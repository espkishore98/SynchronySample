package com.synchrony.service;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.synchrony.entity.User;
import com.synchrony.repository.UserRepository;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class UserServiceImpl implements UserService {
@Autowired
UserRepository userRepo;
@Value("${clientId}")
private String clientId;
@Autowired
RestTemplate restTemplate;

@Override
public ResponseEntity RegisterUser(User user) {
	try {
		if(user !=null && user.getUserName() !=null ) {
			User userDtls=userRepo.getUserByUserName(user.getUserName());
			if(userDtls!=null) {
			return new ResponseEntity("user Already Present", HttpStatus.BAD_REQUEST);
			}
			userRepo.save(user);
	}
	}catch(Exception e) {
		return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
	}
	return new ResponseEntity("Details Saved successfully", HttpStatus.OK);

	
}




	public  void main(Long imageId) throws IOException {
		
		URL url = new URL("https://api.imgur.com/"+imageId+"/image");
		OkHttpClient client = new OkHttpClient().newBuilder()
				  .build();
				MediaType mediaType = MediaType.parse("text/plain");
				RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
				  .build();
				Request request = new Request.Builder()
				  .url("https://api.imgur.com/3/upload")
				  .method("POST", body)
				  .addHeader("Authorization", "Bearer 5eeae49394cd929e299785c8805bd168fc675280")
				  .build();
				Response response = client.newCall(request).execute();
		


		
	}

	

	
}
