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
package com.tencent.devops.store.api.common

import com.tencent.devops.common.api.annotation.BkInterfaceI18n
import com.tencent.devops.common.api.auth.AUTH_HEADER_USER_ID
import com.tencent.devops.common.api.pojo.Page
import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.common.web.annotation.BkField
import com.tencent.devops.common.web.constant.BkStyleEnum
import com.tencent.devops.store.pojo.common.InstallStoreReq
import com.tencent.devops.store.pojo.common.MarketItem
import com.tencent.devops.store.pojo.common.MarketMainItem
import com.tencent.devops.store.pojo.common.StoreDetailInfo
import com.tencent.devops.store.pojo.common.UnInstallReq
import com.tencent.devops.store.pojo.common.enums.RdTypeEnum
import com.tencent.devops.store.pojo.common.enums.StoreSortTypeEnum
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.HeaderParam
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Tag(name = "SERVICE_STORE_COMPONENT", description = "研发商店-SERVICE-组件管理")
@Path("/service/store/components")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface ServiceStoreComponentResource {

    @Operation(summary = "获取研发商店首页组件的数据")
    @Path("/types/{storeType}/component/main/page/list")
    @GET
    @BkInterfaceI18n(
        keyPrefixNames = ["{data.records[*].storeType}", "{data.records[*].storeCode}", "{data.records[*].version}",
            "releaseInfo"]
    )
    fun getMainPageComponents(
        @Parameter(description = "userId", required = true)
        @HeaderParam(AUTH_HEADER_USER_ID)
        userId: String,
        @Parameter(description = "组件类型", required = true)
        @PathParam("storeType")
        @BkField(patternStyle = BkStyleEnum.CODE_STYLE)
        storeType: String,
        @Parameter(description = "页码", required = true)
        @QueryParam("page")
        @BkField(patternStyle = BkStyleEnum.NUMBER_STYLE)
        page: Int = 1,
        @Parameter(description = "每页数量", required = true)
        @QueryParam("pageSize")
        @BkField(patternStyle = BkStyleEnum.PAGE_SIZE_STYLE)
        pageSize: Int = 8
    ): Result<List<MarketMainItem>>

    @Operation(summary = "根据条件查询组件列表")
    @Path("/types/{storeType}/component/list")
    @GET
    @BkInterfaceI18n(
        keyPrefixNames = ["{data.records[*].storeType}", "{data.records[*].storeCode}", "{data.records[*].version}",
            "releaseInfo"]
    )
    @Suppress("LongParameterList")
    fun queryComponents(
        @Parameter(description = "userId", required = true)
        @HeaderParam(AUTH_HEADER_USER_ID)
        userId: String,
        @Parameter(description = "组件类型", required = true)
        @PathParam("storeType")
        @BkField(patternStyle = BkStyleEnum.CODE_STYLE)
        storeType: String,
        @Parameter(description = "项目代码", required = false)
        @QueryParam("projectCode")
        projectCode: String? = null,
        @Parameter(description = "搜索关键字", required = false)
        @QueryParam("keyword")
        @BkField(patternStyle = BkStyleEnum.COMMON_STYLE, required = false)
        keyword: String?,
        @Parameter(description = "分类ID", required = false)
        @QueryParam("classifyId")
        @BkField(patternStyle = BkStyleEnum.ID_STYLE, required = false)
        classifyId: String?,
        @Parameter(description = "标签ID", required = false)
        @QueryParam("labelId")
        @BkField(patternStyle = BkStyleEnum.ID_STYLE, required = false)
        labelId: String?,
        @Parameter(description = "范畴ID", required = false)
        @QueryParam("categoryId")
        @BkField(patternStyle = BkStyleEnum.ID_STYLE, required = false)
        categoryId: String?,
        @Parameter(description = "评分", required = false)
        @QueryParam("score")
        @BkField(patternStyle = BkStyleEnum.NUMBER_STYLE, required = false)
        score: Int?,
        @Parameter(description = "研发来源类型", required = false)
        @QueryParam("rdType")
        rdType: RdTypeEnum?,
        @Parameter(description = "是否推荐标识 true：推荐，false：不推荐", required = false)
        @QueryParam("recommendFlag")
        @BkField(patternStyle = BkStyleEnum.BOOLEAN_STYLE, required = false)
        recommendFlag: Boolean?,
        @Parameter(description = "是否需要更新标识 true：需要，false：不需要", required = false)
        @QueryParam("updateFlag")
        @BkField(patternStyle = BkStyleEnum.BOOLEAN_STYLE, required = false)
        updateFlag: Boolean?,
        @QueryParam("installedFlag")
        @BkField(patternStyle = BkStyleEnum.BOOLEAN_STYLE, required = false)
        installedFlag: Boolean?,
        @Parameter(description = "是否查询项目下组件标识", required = true)
        @QueryParam("queryProjectComponentFlag")
        queryProjectComponentFlag: Boolean = false,
        @Parameter(description = "排序", required = false)
        @QueryParam("sortType")
        sortType: StoreSortTypeEnum? = StoreSortTypeEnum.CREATE_TIME,
        @Parameter(description = "页码", required = true)
        @QueryParam("page")
        @BkField(patternStyle = BkStyleEnum.NUMBER_STYLE)
        page: Int = 1,
        @Parameter(description = "每页数量", required = true)
        @QueryParam("pageSize")
        @BkField(patternStyle = BkStyleEnum.PAGE_SIZE_STYLE)
        pageSize: Int = 10
    ): Result<Page<MarketItem>>

    @Operation(summary = "根据组件ID获取组件详情")
    @GET
    @Path("/types/{storeType}/ids/{storeId}/component/detail")
    @BkInterfaceI18n(
        keyPrefixNames = ["{data.records[*].storeType}", "{data.records[*].storeCode}", "{data.records[*].version}",
            "releaseInfo"]
    )
    fun getComponentDetailInfoById(
        @Parameter(description = "userId", required = true)
        @HeaderParam(AUTH_HEADER_USER_ID)
        userId: String,
        @Parameter(description = "组件类型", required = true)
        @PathParam("storeType")
        @BkField(patternStyle = BkStyleEnum.CODE_STYLE)
        storeType: String,
        @Parameter(description = "组件ID", required = true)
        @PathParam("storeId")
        @BkField(patternStyle = BkStyleEnum.ID_STYLE, required = false)
        storeId: String
    ): Result<StoreDetailInfo?>

    @Operation(summary = "安装组件到项目")
    @POST
    @Path("/types/{storeType}/component/install")
    fun installComponent(
        @Parameter(description = "userId", required = true)
        @HeaderParam(AUTH_HEADER_USER_ID)
        userId: String,
        @Parameter(description = "安装组件到项目请求报文体", required = true)
        installStoreReq: InstallStoreReq
    ): Result<Boolean>

    @Operation(summary = "卸载组件")
    @Path("/projects/{projectCode}/types/{storeType}/codes/{storeCode}/component/uninstall")
    @DELETE
    fun uninstallComponent(
        @Parameter(description = "userId", required = true)
        @HeaderParam(AUTH_HEADER_USER_ID)
        userId: String,
        @Parameter(description = "项目代码", required = true)
        @PathParam("projectCode")
        projectCode: String,
        @Parameter(description = "组件类型", required = true)
        @PathParam("storeType")
        @BkField(patternStyle = BkStyleEnum.CODE_STYLE)
        storeType: String,
        @Parameter(description = "组件代码", required = true)
        @PathParam("storeCode")
        @BkField(patternStyle = BkStyleEnum.CODE_STYLE)
        storeCode: String,
        @Parameter(description = "卸载组件请求包体", required = true)
        unInstallReq: UnInstallReq
    ): Result<Boolean>
}
