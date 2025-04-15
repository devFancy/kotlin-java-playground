package dev.be.core.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Table(name = "posts")
@Entity
public class ExamplePost {

    @Column(name = "id", columnDefinition = "binary(16)")
    @Id
    private UUID id;

    private String title;

    private String contents;

    protected ExamplePost() {
    }

    public ExamplePost(final String title, final String contents) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.contents = contents;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }
}
