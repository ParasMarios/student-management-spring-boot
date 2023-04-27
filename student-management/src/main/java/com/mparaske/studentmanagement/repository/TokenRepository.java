package com.mparaske.studentmanagement.repository;

import com.mparaske.studentmanagement.token.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    @Query("{ 'user._id': ?0, $or: [ { 'expired': false }, { 'revoked': false } ] }")
    List<Token> findAllValidTokensByUser(String userId);

    Optional<Token> findByToken(String token);
}
