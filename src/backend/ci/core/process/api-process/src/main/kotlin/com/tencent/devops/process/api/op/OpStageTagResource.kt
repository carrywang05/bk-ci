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

package com.tencent.devops.process.api.op

import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.process.pojo.PipelineStageTag
import com.tencent.devops.process.pojo.StageTagRequest
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Tag(name = "OP_PIPELINE_STAGE_TAG", description = "OP-流水线-阶段标签")
@Path("/op/pipeline/stage/tag")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface OpStageTagResource {

    @Operation(summary = "添加流水线阶段标签信息")
    @POST
    @Path("/")
    fun add(
        @Parameter(description = "流水线阶段标签请求体", required = true)
        stageTagRequest: StageTagRequest
    ): Result<Boolean>

    @Operation(summary = "更新流水线阶段标签信息")
    @PUT
    @Path("/{id}")
    fun update(
        @Parameter(description = "流水线阶段标签ID", required = true)
        @PathParam("id")
        id: String,
        @Parameter(description = "流水线阶段标签请求体", required = true)
        stageTagRequest: StageTagRequest
    ): Result<Boolean>

    @Operation(summary = "获取所有流水线阶段标签信息")
    @GET
    @Path("/")
    fun listAllStageTags(): Result<List<PipelineStageTag>>

    @Operation(summary = "根据ID获取流水线阶段标签信息")
    @GET
    @Path("/{id}")
    fun getStageTagById(
        @Parameter(description = "流水线阶段标签ID", required = true)
        @PathParam("id")
        id: String
    ): Result<PipelineStageTag?>

    @Operation(summary = "根据ID删除流水线阶段标签信息")
    @DELETE
    @Path("/{id}")
    fun deleteStageTagById(
        @Parameter(description = "流水线阶段标签ID", required = true)
        @PathParam("id")
        id: String
    ): Result<Boolean>
}
