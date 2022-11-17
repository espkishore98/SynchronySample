package com.synchrony.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.Base64;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nimbusds.jwt.SignedJWT;
import com.synchrony.domain.AuthUser;
import com.synchrony.domain.JwtAuthenticationResponse;
import com.synchrony.domain.LoginRequest;
import com.synchrony.entity.User;
import com.synchrony.entity.UserImages;
import com.synchrony.repository.UserImageRepository;
import com.synchrony.repository.UserRepository;
import com.synchrony.security.JwtTokenProvider;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepo;
	@Autowired
	UserImageRepository userImagesRepo;
	@Value("${imgur.clientId}")
	private String clientId;

//public static String generateTokenUrl= "https://api.imgur.com/oauth2/authorize?client_id="+clientId+" &response_type=token&state=";
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private AuthenticationManager authenticationManager;
	private PasswordEncoder passwordEncoder;
	private JwtTokenProvider tokenProvider;

	@Autowired
	public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
			PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
		this.authenticationManager = authenticationManager;
		this.userRepo = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
	}

	public static String getUserExternalId(String authToken) {

		// String token = resolveToken(authToken);
		String token = authToken;
		if (token == null) {
			return "Unauthorized user";
		}
		SignedJWT signedJWT;
		ObjectMapper mapper = new ObjectMapper();
		try {
			signedJWT = SignedJWT.parse(token);
			String object = signedJWT.getPayload().toJSONObject().toJSONString();
			AuthUser user = mapper.readValue(object, AuthUser.class);
			if (System.currentTimeMillis() > user.getSub()) {
				return "unAuthorized User";
			} else {
				return user.getJti();
			}
		} catch (ParseException | JsonProcessingException jpe) {
			LOGGER.info(jpe.getMessage() + " at " + Calendar.getInstance().getTime());
			LOGGER.error(jpe.getMessage(), jpe);

		} catch (Exception e) {
			LOGGER.info(e.getMessage() + " at " + Calendar.getInstance().getTime());
			LOGGER.error(e.getMessage(), e);
		}

		return "unAuthorized user";
	}

	public static String getUserExternalId(HttpServletRequest authToken) {

		String token = resolveToken(authToken);
		if (token == null) {
			return "unAuthorized User";
		}
		SignedJWT signedJWT;
		ObjectMapper mapper = new ObjectMapper();
		try {
			signedJWT = SignedJWT.parse(token);
			String object = signedJWT.getPayload().toJSONObject().toJSONString();
			AuthUser user = mapper.readValue(object, AuthUser.class);
			if (System.currentTimeMillis() > user.getSub()) {
				return "unAuthorized User";
			} else {
				return user.getJti();
			}
		} catch (ParseException | JsonProcessingException jpe) {
			LOGGER.info(jpe.getMessage() + " at " + Calendar.getInstance().getTime());
			LOGGER.error(jpe.getMessage(), jpe);

		} catch (Exception e) {
			LOGGER.info(e.getMessage() + " at " + Calendar.getInstance().getTime());
			LOGGER.error(e.getMessage(), e);
		}

		return "unAuthorized User";
	}

	public static String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	@Override
	public ResponseEntity registerUser(User userRqst) {
		try {
			if (userRqst != null && userRqst.getUserName() != null) {
				// userName is unique
				User userDtls = userRepo.getByuserName(userRqst.getUserName());
				if (userDtls != null) {
					return new ResponseEntity("user Already Present", HttpStatus.BAD_REQUEST);
				}
				User createUser = new User(UUID.randomUUID().toString(), userRqst.getUserName(),

						passwordEncoder.encode(userRqst.getPassword()));
				userRepo.saveAndFlush(createUser);
			}
		} catch (Exception e) {
			return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity("Details Saved successfully", HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getUserById(Long userId) {
		Optional<User> userDtls = null;
		try {
			if (userId != null) {
				// userName is unique
				System.out.println(userRepo.findAll());
				userDtls = userRepo.findById(userId);
				if (!userDtls.isPresent()) {
					return new ResponseEntity("user not found for Id", HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(userDtls.get(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity loginUser(LoginRequest request) {
		LOGGER.info("login user started----->");
		User user = userRepo.getByuserName(request.getUserName());

		String jwt = "";
		try {
			if (user == null) {
				return new ResponseEntity("User not found", HttpStatus.BAD_REQUEST);
			}
			if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
				return new ResponseEntity("Invalid Password", HttpStatus.BAD_REQUEST);
			}
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), request.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			jwt = tokenProvider.generateToken(authentication, user);
			LOGGER.info("login ended ===>", jwt);
		} catch (Exception e) {
			LOGGER.error("something went wrong ===>",e.getMessage());
			return new ResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(
				new JwtAuthenticationResponse(jwt, "Bearer", user.getUserName(), user.getExternalId()), HttpStatus.OK);

	}

	public File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public ResponseEntity upload(MultipartFile file, String authToken) {
		RestTemplate restTemplate = new RestTemplate();
		UserImages userImages = new UserImages();
		String authToken2 = authToken.substring(7);
		String userName = getUserExternalId(authToken2);
		try {
			User user = userRepo.getByuserName(userName);
			URI url = new URI("https://api.imgur.com/3/image");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);
			headers.set("Authorization", clientId);
			byte[] image = Base64.getEncoder().encode(file.getBytes());
			String result = new String(image);
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("image", result);
			HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
			ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
			LinkedHashMap<String, Object> resBody = (LinkedHashMap<String, Object>) response.getBody();
			if (resBody.get("status").equals(200)) {
				Gson gson = new Gson();
				Map<Object, Object> attributes = gson.fromJson(gson.toJson(resBody.get("data")), Map.class);
				if (attributes.containsKey("id")) {
					userImages.setImageHash(attributes.get("id").toString());
					userImages.setUserId(user.getId());
					userImagesRepo.save(userImages);
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new ResponseEntity(userImages, HttpStatus.OK);
	}
	
	public ResponseEntity findAllImgsByUserId(String authToken) {
		String authToken2 = authToken.substring(7);
		String userName = getUserExternalId(authToken2);
		try {
			User user = userRepo.getByuserName(userName);
			LOGGER.info("find ALL images for user",user.getUserName());
			if (user == null) {
				return new ResponseEntity("something went wrong", HttpStatus.BAD_REQUEST);
			}
			List<UserImages> userImages = userImagesRepo.getByUserId(user.getId());
			return new ResponseEntity(userImages, HttpStatus.OK);
		}catch(Exception e) {
			LOGGER.error("something went wrong====>",e.getMessage());
			return new ResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
	}

	@Override
	public ResponseEntity findByImageByKey(String imageKey) {
		LOGGER.info("Find image by key started====>",imageKey);
		try {
			URI url = new URI("https://api.imgur.com/3/image/");
			String mainUrl = url.toString().concat(imageKey);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
			headers.set("Authorization", clientId);
//			byte[] image = Base64.getEncoder().encode(file.getBytes());
//			String result = new String(image);
//			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//			body.add("image", result);
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder().url(mainUrl).method("GET", null)
					.addHeader("Authorization", clientId).build();
			Response response = client.newCall(request).execute();

			return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Something went wrong",e.getMessage());
			System.out.println(e.getMessage());
			return new ResponseEntity("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		

	}
}
