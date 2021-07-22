package jp.co.stnet.cms.example.domain.model.lombok;

import java.util.List;
import java.util.Map;

public class LombokEntity extends AbstractLombokEntity {

    private String field101;
    private Integer field102;
    private List<String> list103;
    private Map<String, String> map104;
    private LombokChildEntity childField105;
    private List<LombokChildEntity> childList106;

    public LombokEntity() {
    }

    public String getField101() {
        return this.field101;
    }

    public Integer getField102() {
        return this.field102;
    }

    public List<String> getList103() {
        return this.list103;
    }

    public Map<String, String> getMap104() {
        return this.map104;
    }

    public LombokChildEntity getChildField105() {
        return this.childField105;
    }

    public List<LombokChildEntity> getChildList106() {
        return this.childList106;
    }

    public void setField101(String field101) {
        this.field101 = field101;
    }

    public void setField102(Integer field102) {
        this.field102 = field102;
    }

    public void setList103(List<String> list103) {
        this.list103 = list103;
    }

    public void setMap104(Map<String, String> map104) {
        this.map104 = map104;
    }

    public void setChildField105(LombokChildEntity childField105) {
        this.childField105 = childField105;
    }

    public void setChildList106(List<LombokChildEntity> childList106) {
        this.childList106 = childList106;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LombokEntity)) return false;
        final LombokEntity other = (LombokEntity) o;
        if (!other.canEqual(this)) return false;
        final Object this$field101 = this.getField101();
        final Object other$field101 = other.getField101();
        if (this$field101 == null ? other$field101 != null : !this$field101.equals(other$field101)) return false;
        final Object this$field102 = this.getField102();
        final Object other$field102 = other.getField102();
        if (this$field102 == null ? other$field102 != null : !this$field102.equals(other$field102)) return false;
        final Object this$list103 = this.getList103();
        final Object other$list103 = other.getList103();
        if (this$list103 == null ? other$list103 != null : !this$list103.equals(other$list103)) return false;
        final Object this$map104 = this.getMap104();
        final Object other$map104 = other.getMap104();
        if (this$map104 == null ? other$map104 != null : !this$map104.equals(other$map104)) return false;
        final Object this$childField105 = this.getChildField105();
        final Object other$childField105 = other.getChildField105();
        if (this$childField105 == null ? other$childField105 != null : !this$childField105.equals(other$childField105))
            return false;
        final Object this$childList106 = this.getChildList106();
        final Object other$childList106 = other.getChildList106();
        return this$childList106 == null ? other$childList106 == null : this$childList106.equals(other$childList106);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LombokEntity;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $field101 = this.getField101();
        result = result * PRIME + ($field101 == null ? 43 : $field101.hashCode());
        final Object $field102 = this.getField102();
        result = result * PRIME + ($field102 == null ? 43 : $field102.hashCode());
        final Object $list103 = this.getList103();
        result = result * PRIME + ($list103 == null ? 43 : $list103.hashCode());
        final Object $map104 = this.getMap104();
        result = result * PRIME + ($map104 == null ? 43 : $map104.hashCode());
        final Object $childField105 = this.getChildField105();
        result = result * PRIME + ($childField105 == null ? 43 : $childField105.hashCode());
        final Object $childList106 = this.getChildList106();
        result = result * PRIME + ($childList106 == null ? 43 : $childList106.hashCode());
        return result;
    }

    public String toString() {
        return "LombokEntity(field101=" + this.getField101() + ", field102=" + this.getField102() + ", list103=" + this.getList103() + ", map104=" + this.getMap104() + ", childField105=" + this.getChildField105() + ", childList106=" + this.getChildList106() + ")";
    }
}
