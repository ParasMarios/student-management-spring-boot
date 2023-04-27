package com.mparaske.studentmanagement.token;

import com.mparaske.studentmanagement.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tokens")
public class Token {

    @Id
    private String id;
    private String token;
    private TokenType type;

    private boolean expired;

    private boolean revoked;

    @DBRef
    private User user;
}
