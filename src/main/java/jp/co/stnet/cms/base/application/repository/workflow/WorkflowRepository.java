package jp.co.stnet.cms.base.application.repository.workflow;


import jp.co.stnet.cms.base.domain.model.workflow.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
}
