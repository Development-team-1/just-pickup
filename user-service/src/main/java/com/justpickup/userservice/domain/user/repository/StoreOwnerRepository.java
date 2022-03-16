package com.justpickup.userservice.domain.user.repository;

import com.justpickup.userservice.domain.user.entity.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreOwnerRepository extends JpaRepository<StoreOwner, Long> {
}
