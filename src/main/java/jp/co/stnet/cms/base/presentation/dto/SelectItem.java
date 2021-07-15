package jp.co.stnet.cms.base.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectItem implements Serializable {

    private String value;
    private String label;

}
