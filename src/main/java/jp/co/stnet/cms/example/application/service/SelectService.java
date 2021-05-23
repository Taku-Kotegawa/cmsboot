package jp.co.stnet.cms.example.application.service;


import jp.co.stnet.cms.base.application.service.NodeRevIService;
import jp.co.stnet.cms.example.domain.model.select.Select;
import jp.co.stnet.cms.example.domain.model.select.SelectRevision;

public interface SelectService extends NodeRevIService<Select, SelectRevision, Long> {
}
