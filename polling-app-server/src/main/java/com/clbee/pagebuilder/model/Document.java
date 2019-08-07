package com.clbee.pagebuilder.model;

import com.clbee.pagebuilder.model.audit.UserDateAudit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pb_documents")
public class Document extends UserDateAudit {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 3)
    private String deadmark;

    @Size(max = 2048)
    private String preference;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length=10000000)
    private String contents;
}
