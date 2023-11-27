package com.example.cloud.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.cloud.auth.info.OAuth2UserInfo;
import com.example.cloud.converter.ModelConverter;
import com.example.cloud.domain.mapping.Model;
import com.example.cloud.domain.mapping.Role;
import com.example.cloud.domain.mapping.User;
import com.example.cloud.repository.ModelRepository;
import com.example.cloud.service.ModelService;
import com.example.cloud.web.dto.model.ModelResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.cloud.converter.ModelConverter.toUserModelDetailDto;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final AmazonS3 amazonS3;

    private String bucket = "cloudprojectshi";
    private final ModelRepository modelRepository;

    //private final ModelConverter modelConverter;

    @Override
    public Model createUserModel(String path, String name, User user){

        Model model = Model.builder()
                .user(user)
                .path(path)
                .share_check(0)
                .name(name).build();

        return modelRepository.save(model);
    }

    @Override
    public ModelResponseDto.ListUserModelDetailDto findUserModelList(Long user_id){

        List<Model> modelList = modelRepository.findByUserId(user_id);
        List<ModelResponseDto.UserModelDetailDto> userModelDetailDtoList = new ArrayList<>();
        System.out.println(modelList);
        for(int i = 0; i<modelList.size();i++){

            userModelDetailDtoList.add(toUserModelDetailDto(modelList.get(i)));
        }
        return new ModelResponseDto.ListUserModelDetailDto(userModelDetailDtoList);
    }

    @Override
    public ModelResponseDto.ListUserModelDetailDto findModelList(Long modelId,String name){

        Pageable pageable = PageRequest.of(0,10);
        List<Model> modelList = new ArrayList<>();
        if(name==null) {
            List<Model> modelLists = modelRepository.findByUserIdWhereModelId(modelId, pageable);
            modelList = modelLists;
        }
        if(name!=null){
            List<Model> modelLists = modelRepository.findByUserIdWhereModelIdAndName(modelId, pageable,name);
            modelList = modelLists;
        }
        List<ModelResponseDto.UserModelDetailDto> userModelDetailDtoList = new ArrayList<>();
        System.out.println(modelList);
        for(int i = 0; i<modelList.size();i++){

            userModelDetailDtoList.add(toUserModelDetailDto(modelList.get(i)));
        }
        return new ModelResponseDto.ListUserModelDetailDto(userModelDetailDtoList);

    }

    @Override
    public ModelResponseDto.UserModelDetailDto findModelDetail(Long modelId){

        Optional<Model> model = modelRepository.findById(modelId);

        if(model==null){
            return null;
        }
        return toUserModelDetailDto(model.get());

    }

    @Override
    public Boolean updateUserModel(Long modelId, String name, String description,int share){

        if(modelId==null){
            return false;
        }
        if(name != null){
            modelRepository.updateName(modelId, name);
        }
        if(description != null){
            modelRepository.updateDescription(modelId,description);
        }
        if(share ==0 || share == 1){
            modelRepository.updateShareCheck(modelId,share);
        }
        return true;
    }

    @Override
    public String updateUserModelImage(MultipartFile file, Long modelId){

        String fileName = file.getOriginalFilename();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(bucket, fileName, file.getInputStream(), objectMetadata);

        } catch (IOException e) {
            return null;
        }

        String url = amazonS3.getUrl(bucket, fileName).toString();
        System.out.println(modelId+"url:"+url);
        modelRepository.updateImageUrl(modelId, url);

        return amazonS3.getUrl(bucket, fileName).toString();


    }

    @Override
    public Boolean updateUserModelId(Long modelId){

        Optional<Model> model = modelRepository.findById(modelId);

        Model newmodel = Model.builder()
                .path(model.get().getPath())
                .user(model.get().getUser())
                .share_check(model.get().getShare_check())
                .description(model.get().getDescription())
                .name(model.get().getName())
                .image_url(model.get().getImage_url())
                .build();

        modelRepository.save(newmodel);

        modelRepository.deleteById(modelId);

        return true;
    }

}
