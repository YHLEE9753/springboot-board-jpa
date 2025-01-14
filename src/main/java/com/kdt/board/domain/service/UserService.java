package com.kdt.board.domain.service;

import com.kdt.board.domain.converter.Converter;
import com.kdt.board.domain.dto.UserDto;
import com.kdt.board.domain.model.User;
import com.kdt.board.domain.repository.UserRepository;
import com.kdt.board.global.exception.NotFoundEntityByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final Converter converter;

    @Transactional
    public Long save(UserDto.SaveRequest dto) {
        User user = converter.convertUser(dto);
        User entity = userRepository.save(user);
        return entity.getId();
    }

    public UserDto.Response findById(Long id) {
        return userRepository.findById(id)
                .map(converter::convertUserDto)
                .orElseThrow(() -> new NotFoundEntityByIdException("User Entity 를 찾을 수 없습니다. userID : " + id));
    }

    public Page<UserDto.Response> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(converter::convertUserDto);
    }

    @Transactional
    public UserDto.Response update(UserDto.UpdateRequest dto) {
        User user = userRepository.findById(dto.id())
                .orElseThrow(() -> new NotFoundEntityByIdException("User Entity 를 찾을 수 없습니다. userID : " + dto.id()));
        user.updateUser(dto.name(), dto.age(), dto.hobby());
        return converter.convertUserDto(user);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public long count() {
        return userRepository.count();
    }
}
