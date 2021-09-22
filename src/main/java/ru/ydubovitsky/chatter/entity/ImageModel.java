package ru.ydubovitsky.chatter.entity;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Data
@Entity
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
//    @Column(columnDefinition = "BLOB")
    private byte[] byteImage;

    //Тут нет прямой зависимости между изображением из user-ом или post-ом
    @JsonIgnore
    private Long userId;

    @JsonIgnore // Эти данные не передаются клиенту
    private Long postId;

}
