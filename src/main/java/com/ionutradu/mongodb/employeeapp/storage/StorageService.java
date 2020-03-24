package com.ionutradu.mongodb.employeeapp.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ionutradu.mongodb.employeeapp.documents.Employee;
import com.ionutradu.mongodb.employeeapp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
public class StorageService {
        @Autowired
        EmployeeRepository employeeRepository;

        private String curentDir = "src/main/java/com/ionutradu/mongodb/employeeapp/storage";

        public String uploadFile(MultipartFile file) {

                try {
                        Path copyLocation = Paths
                                .get(curentDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
                        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Could not store file " + file.getOriginalFilename()
                                + ". Please try again!");
                }
                return file.getOriginalFilename();
        }

        public List<Employee> readFile(String path) {
                String fullPath = "src/main/java/com/ionutradu/mongodb/employeeapp/storage/"+path;
                // read JSON and load json
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<List<Employee>> typeReference = new TypeReference<List<Employee>>(){};
                InputStream inputStream = TypeReference.class.getResourceAsStream(path);
                try {
                        System.err.println(fullPath);
                        List<Employee> employees = mapper.readValue(fullPath,typeReference);
                        System.out.println("Users Saved!");
                        return employees;
                } catch (IOException e){
                        System.out.println("Unable to save users: " + e.getMessage());
                }

                return null;
        }

}
