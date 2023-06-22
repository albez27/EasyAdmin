package com.albez.web.Repository;

import com.albez.web.Entity.Administrator;
import com.albez.web.Entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SettingsRepository extends JpaRepository<Settings, Integer> {

    Settings getSettingsByAdministrator_IdVk(String idVk);


    @Query("update Settings s set s.theme = ?1 where s.id = ?2")
    @Modifying
    void changeSettingTheme(int theme, Long id);

}
