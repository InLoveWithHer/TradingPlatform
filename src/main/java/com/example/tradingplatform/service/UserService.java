package com.example.tradingplatform.service;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.entity.User;
import com.example.tradingplatform.exception.ResNotFoundException;
import com.example.tradingplatform.reposiroty.AdvertisementsRepository;
import com.example.tradingplatform.reposiroty.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdvertisementsRepository advertisementRepository;

    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, AdvertisementsRepository advertisementRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.advertisementRepository = advertisementRepository;
    }

    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }


    public User getUserById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new ResNotFoundException("User not found with id " + id));
    }

    public User getByEmail(String email) {
        Optional<User> user = usersRepository.findByEmail(email);
        return user.orElse(new User());
    }

    public void saveUser(User user) {
        usersRepository.save(user);
    }

    public Page<Advertisement> getAdvertisementsForUser(User user, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return advertisementRepository.findAllByUserId(user.getId(), pageable);
    }

    public void create(User user) {
        if (usersRepository.findByEmail(user.getEmail()).isPresent()) {

        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            usersRepository.save(user);
        }
    }

    public void deleteUserById(Long id) {
        usersRepository.deleteById(id);
    }

    public User updateUser(Long id, User user) {
        User existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with given id does not exist"));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        return usersRepository.save(existingUser);
    }

    public boolean isEmailExists(String email) {
        return usersRepository.existsByEmail(email);
    }

    public Long getUserIdByEmail(String email) {
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getId();
        } else {
            return null; // Возвращаем null, если пользователь не найден
        }
    }

}
