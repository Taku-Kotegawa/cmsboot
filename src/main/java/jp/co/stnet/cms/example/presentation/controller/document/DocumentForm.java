package jp.co.stnet.cms.example.presentation.controller.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentForm implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private Long version;

    @NotNull
    private String status;


    private Collection<@Valid FileForm> files = new ArrayList<>();

    public interface Create {
    }

    public interface Update {
    }

}
