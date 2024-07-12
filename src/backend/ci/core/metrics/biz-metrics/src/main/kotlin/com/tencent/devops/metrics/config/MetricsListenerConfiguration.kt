/*
 * Tencent is pleased to support the open source community by making BK-CI 蓝鲸持续集成平台 available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-CI 蓝鲸持续集成平台 is licensed under the MIT license.
 *
 * A copy of the MIT License is included in this file.
 *
 *
 * Terms of the MIT License:
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.devops.metrics.config

import com.tencent.devops.common.event.annotation.EventConsumer
import com.tencent.devops.common.event.pojo.measure.BuildEndMetricsBroadCastEvent
import com.tencent.devops.common.event.pojo.measure.DispatchJobMetricsEvent
import com.tencent.devops.common.event.pojo.measure.LabelChangeMetricsBroadCastEvent
import com.tencent.devops.common.event.pojo.measure.ProjectUserDailyEvent
import com.tencent.devops.common.event.pojo.measure.QualityReportEvent
import com.tencent.devops.common.stream.ScsConsumerBuilder
import com.tencent.devops.common.stream.constants.StreamBinding
import com.tencent.devops.metrics.listener.BuildEndMetricsDataReportListener
import com.tencent.devops.metrics.listener.DispatchJobMetricsListener
import com.tencent.devops.metrics.listener.LabelChangeMetricsDataSyncListener
import com.tencent.devops.metrics.listener.ProjectUserDailyMetricsListener
import com.tencent.devops.metrics.service.MetricsDataReportService
import com.tencent.devops.metrics.service.MetricsThirdPlatformDataReportFacadeService
import com.tencent.devops.metrics.service.SyncPipelineRelateLabelDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricsListenerConfiguration {
    @Bean
    fun buildEndMetricsDataReportListener(
        @Autowired metricsDataReportService: MetricsDataReportService
    ) = BuildEndMetricsDataReportListener(
        metricsDataReportService = metricsDataReportService
    )

    @EventConsumer
    fun buildEndDataReportListener(
        @Autowired listener: BuildEndMetricsDataReportListener
    ) = ScsConsumerBuilder.build<BuildEndMetricsBroadCastEvent> { listener.execute(it) }

    @Bean
    fun labelChangeMetricsDataSyncListener(
        @Autowired syncPipelineRelateLabelDataService: SyncPipelineRelateLabelDataService
    ) = LabelChangeMetricsDataSyncListener(
        syncPipelineRelateLabelDataService = syncPipelineRelateLabelDataService
    )

    @EventConsumer
    fun labelChangeDataSyncListener(
        @Autowired listener: LabelChangeMetricsDataSyncListener
    ) = ScsConsumerBuilder.build<LabelChangeMetricsBroadCastEvent> { listener.execute(it) }

    @EventConsumer
    fun metricsQualityDailyReportListener(
        @Autowired thirdPlatformDataReportFacadeService: MetricsThirdPlatformDataReportFacadeService
    ) = ScsConsumerBuilder.build<QualityReportEvent> {
        thirdPlatformDataReportFacadeService.metricsQualityDataReport(it)
    }

    @EventConsumer
    fun metricsDispatchJobReportListener(
        @Autowired dispatchJobMetricsListener: DispatchJobMetricsListener
    ) = ScsConsumerBuilder.build<DispatchJobMetricsEvent> { dispatchJobMetricsListener.execute(it) }

    @EventConsumer
    fun metricsProjectUserDailyListener(
        @Autowired projectUserDailyMetricsListener: ProjectUserDailyMetricsListener
    ) = ScsConsumerBuilder.build<ProjectUserDailyEvent> { projectUserDailyMetricsListener.execute(it) }
}
