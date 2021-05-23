package jp.co.stnet.cms.example.application.service;


import jp.co.stnet.cms.base.application.service.NodeRevIService;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntity;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntityRevision;

public interface SimpleEntityService extends NodeRevIService<SimpleEntity, SimpleEntityRevision, Long> {
}
