package ru.soular.ewm.main;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.soular.ewm.main.category.dto.CategoryDto;
import ru.soular.ewm.main.category.model.Category;
import ru.soular.ewm.main.event.dto.EventFullDto;
import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.user.dto.UserDto;
import ru.soular.ewm.main.user.dto.UserShortDto;
import ru.soular.ewm.main.user.model.User;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(User.class, UserDto.class);
        mapper.createTypeMap(User.class, UserShortDto.class);
        mapper.createTypeMap(Category.class, CategoryDto.class);
        mapper.createTypeMap(Event.class, EventFullDto.class);

        return mapper;
    }
}
