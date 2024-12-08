package com.example.devansh.Taskmanager.services.impl;

import com.example.devansh.Taskmanager.entities.User;
import com.example.devansh.Taskmanager.exceptions.ResourceNotFoundException;
import com.example.devansh.Taskmanager.payloads.UserDto;
import com.example.devansh.Taskmanager.repositories.UserRepo;
import com.example.devansh.Taskmanager.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

@Autowired
      private UserRepo userRepo;

@Autowired
   private ModelMapper modelMapper;


        @Override
        public UserDto createUser(UserDto userDto) {
            User user = this.modelMapper.map(userDto, User.class);
            User savedUser = userRepo.save(user);
            return this.modelMapper.map(savedUser, UserDto.class);
        }

        @Override
        public List<UserDto> getAllUsers() {
            return userRepo.findAll().stream()
                    .map(user -> modelMapper.map(user, UserDto.class))
                    .collect(Collectors.toList());
        }

        @Override
        public UserDto getUserById(Long id) {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User ","user id" , id));
            return modelMapper.map(user, UserDto.class);
        }

        @Override
        public UserDto updateUser(Long id, UserDto userDto) {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User" ,"user Id", id));

            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setTimezone(userDto.getTimezone());
            user.setIsActive(userDto.getIsActive());

            User updatedUser = userRepo.save(user);
            return modelMapper.map(updatedUser, UserDto.class);
        }

        @Override
        public void deleteUser(Long id) {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User","user id", id));
            userRepo.delete(user);
        }
}
