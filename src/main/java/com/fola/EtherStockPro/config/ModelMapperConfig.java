package com.fola.EtherStockPro.config;

import com.fola.EtherStockPro.DTO.UserDTO;
import com.fola.EtherStockPro.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)// to map private fields
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) //to access private field
                .setMatchingStrategy(MatchingStrategies.STANDARD); // standard name matches

        return  modelMapper;
    }
}
