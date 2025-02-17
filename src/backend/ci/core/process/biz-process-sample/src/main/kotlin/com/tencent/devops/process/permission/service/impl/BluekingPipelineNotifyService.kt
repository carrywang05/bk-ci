package com.tencent.devops.process.permission.service.impl

import com.tencent.devops.process.engine.service.PipelineNotifyService
import com.tencent.devops.process.engine.service.PipelineRepositoryService
import com.tencent.devops.process.notify.command.NotifyCmd
import com.tencent.devops.process.service.BuildVariableService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean

@Configurable
@ConditionalOnMissingBean(PipelineNotifyService::class)
class BluekingPipelineNotifyService @Autowired constructor(
    override val buildVariableService: BuildVariableService,
    override val pipelineRepositoryService: PipelineRepositoryService
) : PipelineNotifyService(
    buildVariableService,
    pipelineRepositoryService
) {
    override fun addExtCmd(): MutableList<NotifyCmd>? {
        return null
    }
}
