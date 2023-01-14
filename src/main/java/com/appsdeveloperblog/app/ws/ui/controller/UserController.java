package com.appsdeveloperblog.app.ws.ui.controller;

import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

import Exceptions.UserServiceException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users") //http:localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;
    @GetMapping(path = "/{id}",
    produces = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id){
        UserRest returnValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE},
                 produces = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE})
    public  UserRest createUser(@RequestBody UserDetailsRequestModel usereDtails)throws Exception  {
        //function return type
        UserRest returnValue = new UserRest();
        if(usereDtails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(usereDtails , userDto);

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(usereDtails, UserDto.class);

        UserDto createUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createUser, returnValue);


        return  returnValue;
    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser( @PathVariable String userId ,@RequestBody UserDetailsRequestModel usereDtails){
        UserRest returnValue = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(usereDtails , userDto);
        UserDto updateUser = userService.updateUser(userId,userDto);
        BeanUtils.copyProperties(updateUser, returnValue);

        return  returnValue;

    }

    @DeleteMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id){

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());


        return returnValue;
    }
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value="page", defaultValue = "0") int page, @RequestParam(value = "limit",defaultValue = "25") int limit){
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page,limit);

        for (UserDto userDto:users){
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }
        return  returnValue;
    }
}
