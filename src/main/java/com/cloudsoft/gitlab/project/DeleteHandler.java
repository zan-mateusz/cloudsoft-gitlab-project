package com.cloudsoft.gitlab.project;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Project;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.Objects;

public class DeleteHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        // TODO : put your code here
        GitLabApi gitLabApi;
        try{
            if (Objects.isNull(model.getServer())){
                return ProgressEvent.<ResourceModel, CallbackContext>builder()
                        .resourceModel(model)
                        .status(OperationStatus.SUCCESS)
                        .build();
            } else if (!Objects.isNull(model.getToken())){
                gitLabApi = new GitLabApi(model.getServer(), model.getToken());
            } else if(!(Objects.isNull(model.getUserID()) || Objects.isNull(model.getUserPassword()))){
                gitLabApi = GitLabApi.oauth2Login(model.getServer(), model.getUserID(), model.getUserPassword());
            } else {
                return ProgressEvent.<ResourceModel, CallbackContext>builder()
                        .resourceModel(model)
                        .status(OperationStatus.SUCCESS)
                        .build();
            }
            if (!Objects.isNull(model.getName())){
                gitLabApi.getProjectApi().deleteProject(model.getName());
            }

        } catch (Exception e){
            e.printStackTrace();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModel(model)
                    .status(OperationStatus.SUCCESS)
                    .build();
        }

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
            .resourceModel(model)
            .status(OperationStatus.SUCCESS)
            .build();
    }
}
