package com.example.sautiyangu.ServiceImpl;

import com.example.sautiyangu.Constant.SautiConstants;
import com.example.sautiyangu.Dao.UserDao;
import com.example.sautiyangu.Jwt.CustomerUserDetailsServvice;
import com.example.sautiyangu.Jwt.JwtFilter;
import com.example.sautiyangu.Jwt.JwtUtil;
import com.example.sautiyangu.Pojo.User;
import com.example.sautiyangu.Services.UserService;
import com.example.sautiyangu.Utills.EmailUtils;
import com.example.sautiyangu.Utills.SautiUtills;
import com.example.sautiyangu.Wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    CustomerUserDetailsServvice customerUserDetailsServvice;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Iside signup {}", requestMap);
            try {

                if (validateSignUpMap(requestMap)) {
                    User user = userDao.findByEmailId(requestMap.get("email"));

                    if (Objects.isNull(user)) {
                        userDao.save(getUserFromMap(requestMap));
                        return SautiUtills.getResponseEntity("Successfully Registered", HttpStatus.OK);
                    } else {
                        return SautiUtills.getResponseEntity("Email already Exist", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return SautiUtills.getResponseEntity(SautiConstants.INVALID_DATA, HttpStatus.BAD_GATEWAY);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        return SautiUtills.getResponseEntity(SautiConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String,String> requestMap){
       if(requestMap.containsKey("name")  && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
           return  true;
       }else {
           return  false;
       }
    }

    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
       log.info("Inside login");
       try {
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
           );
           if (authentication.isAuthenticated()){
               if (customerUserDetailsServvice.getUserDetails().getStatus().equalsIgnoreCase("true")){
                   return new ResponseEntity<String>("{\"token\";\""+
                         jwtUtil.generateToken(customerUserDetailsServvice.getUserDetails().getEmail(),
                                 customerUserDetailsServvice.getUserDetails().getRole()) +
                                 "\"}",
                   HttpStatus.OK);
               }
               else {
                   return new ResponseEntity<String>("{\"message\":\""+"Wait for admin proval."+"\"}",HttpStatus.BAD_REQUEST);
               }
           }
       }catch (Exception ex){
            log.error("{}",ex);
       }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credential."+"\"}",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new  ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                Optional<User> optional =  userDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()){
                    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
                    return SautiUtills.getResponseEntity("User Status Updated Successfully",HttpStatus.OK);
                }else {
                    return SautiUtills.getResponseEntity("User id does not exist",HttpStatus.OK);
                }
            }else{
                return SautiUtills.getResponseEntity(SautiConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SautiUtills.getResponseEntity(SautiConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());

        if (status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Approved","USER:- "+user+"\n is approved by \nADMIN:-"+jwtFilter.getCurrentUser(),allAdmin);
        }else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Disabled","USER:- "+user+"\n is disabled by \nADMIN:-"+jwtFilter.getCurrentUser(),allAdmin);
        }
    }


}
