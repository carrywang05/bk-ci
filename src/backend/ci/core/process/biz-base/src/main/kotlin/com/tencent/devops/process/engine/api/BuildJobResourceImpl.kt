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

package com.tencent.devops.process.engine.api

import com.tencent.devops.common.api.exception.ParamBlankException
import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.common.pipeline.enums.BuildStatus
import com.tencent.devops.common.web.RestResource
import com.tencent.devops.engine.api.BuildJobResource
import com.tencent.devops.engine.api.pojo.HeartBeatInfo
import com.tencent.devops.process.engine.service.vmbuild.EngineVMBuildService
import com.tencent.devops.process.pojo.BuildTask
import com.tencent.devops.process.pojo.BuildTaskResult
import com.tencent.devops.process.pojo.BuildVariables
import org.apache.commons.lang3.math.NumberUtils
import org.springframework.beans.factory.annotation.Autowired

@Suppress("UNUSED")
@RestResource
class BuildJobResourceImpl @Autowired constructor(
    private val vMBuildService: EngineVMBuildService
) : BuildJobResource {

    override fun jobStarted(
        buildId: String,
        vmSeqId: String,
        vmName: String,
        retryCount: String
    ): Result<BuildVariables> {
        checkParam(buildId, vmSeqId, vmName, retryCount)
        return Result(vMBuildService.buildVMStarted(
            buildId = buildId,
            vmSeqId = vmSeqId,
            vmName = vmName,
            retryCount = retryCount.toInt()
        ))
    }

    override fun claimTask(buildId: String, vmSeqId: String, vmName: String): Result<BuildTask> {
        checkParam(buildId = buildId, vmSeqId = vmSeqId, vmName = vmName)
        return Result(vMBuildService.buildClaimTask(buildId = buildId, vmSeqId = vmSeqId, vmName = vmName))
    }

    override fun completeTask(
        buildId: String,
        vmSeqId: String,
        vmName: String,
        result: BuildTaskResult
    ): Result<Boolean> {
        checkParam(buildId = buildId, vmSeqId = vmSeqId, vmName = vmName)
        vMBuildService.buildCompleteTask(buildId = buildId, vmSeqId = vmSeqId, vmName = vmName, result = result)
        return Result(true)
    }

    override fun jobEnd(buildId: String, vmSeqId: String, vmName: String): Result<Boolean> {
        checkParam(buildId = buildId, vmSeqId = vmSeqId, vmName = vmName)
        return Result(vMBuildService.buildEndTask(buildId = buildId, vmSeqId = vmSeqId, vmName = vmName))
    }

    override fun jobTimeout(
        projectId: String,
        pipelineId: String,
        buildId: String,
        vmSeqId: String
    ): Result<Boolean> {
        return Result(
            data = vMBuildService.setStartUpVMStatus(
                projectId = projectId,
                pipelineId = pipelineId,
                buildId = buildId,
                vmSeqId = vmSeqId,
                buildStatus = BuildStatus.EXEC_TIMEOUT
            )
        )
    }

    override fun jobHeartbeat(
        buildId: String,
        vmSeqId: String,
        vmName: String
    ): Result<Boolean> {
        checkParam(buildId = buildId, vmSeqId = vmSeqId, vmName = vmName)
        return Result(
            data = vMBuildService.heartbeat(
                buildId = buildId,
                vmSeqId = vmSeqId,
                vmName = vmName
            )
        )
    }

    override fun jobHeartbeatV1(
        projectId: String,
        buildId: String,
        vmSeqId: String,
        vmName: String
    ): Result<HeartBeatInfo> {
        checkParam(buildId = buildId, vmSeqId = vmSeqId, vmName = vmName)
        return Result(
            data = vMBuildService.heartbeatV1(
                projectId = projectId,
                buildId = buildId,
                vmSeqId = vmSeqId,
                vmName = vmName
            )
        )
    }

    companion object {
        private fun checkParam(buildId: String, vmSeqId: String, vmName: String, retryCount: String? = null) {
            if (buildId.isBlank()) {
                throw ParamBlankException("Invalid buildId")
            }

            if (vmSeqId.isBlank()) {
                throw ParamBlankException("Invalid vmSeqId")
            }

            if (vmName.isBlank()) {
                throw ParamBlankException("Invalid vmName")
            }

            if (!retryCount.isNullOrBlank() && !NumberUtils.isDigits(retryCount)) {
                throw ParamBlankException("Invalid Int retryCount: $retryCount")
            }
        }
    }
}
