package tgbot.useradd.registr.KBOtgbotsecurity.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tgbot.useradd.registr.KBOtgbotsecurity.entity.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByTelegramId(Long telegramId);
}