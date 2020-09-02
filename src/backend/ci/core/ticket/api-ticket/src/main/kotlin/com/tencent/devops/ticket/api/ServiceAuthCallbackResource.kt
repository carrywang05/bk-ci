package com.tencent.devops.ticket.api

import com.tencent.devops.common.api.pojo.Page
import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.ticket.pojo.Cert
import com.tencent.devops.ticket.pojo.Credential
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Api(tags = ["SERVICE_CREDENTIAL"], description = "服务-证书资源-权限中心")
@Path("/service/auth/ticket")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface ServiceAuthCallbackResource {

    @ApiOperation("其他服务获取凭据列表")
    @Path("/{projectId}/credential")
    @GET
    fun listCredential(
        @ApiParam("项目ID", required = true)
        @PathParam("projectId")
        projectId: String,
        @ApiParam("第几页", required = false, defaultValue = "1")
        @QueryParam("page")
        page: Int?,
        @ApiParam("每页多少条", required = false, defaultValue = "20")
        @QueryParam("pageSize")
        pageSize: Int?
    ): Result<Page<Credential>?>

    @ApiOperation("获取凭证信息")
    @Path("/{projectId}/credential/getInfos")
    @GET
    fun getCredentialInfos(
        @ApiParam("项目ID", required = true)
        @PathParam("projectId")
        projectId: String,
        @ApiParam("凭证ID串", required = true)
        @QueryParam("credentialIds")
        credentialIds: Set<String>
    ): Result<List<Credential>?>

    @ApiOperation("其他服务获取证书列表")
    @Path("/{projectId}/cert")
    @GET
    fun listCert(
        @ApiParam("项目ID", required = true)
        @PathParam("projectId")
        projectId: String,
        @ApiParam("第几页", required = false, defaultValue = "1")
        @QueryParam("page")
        page: Int?,
        @ApiParam("每页多少条", required = false, defaultValue = "20")
        @QueryParam("pageSize")
        pageSize: Int?
    ): Result<Page<Cert>?>

    @ApiOperation("获取证书信息")
    @Path("/{projectId}/cert/getInfos")
    @GET
    fun getCertInfos(
        @ApiParam("项目ID", required = true)
        @PathParam("projectId")
        projectId: String,
        @ApiParam("证书ID串", required = true)
        @QueryParam("certIds")
        certIds: Set<String>
    ): Result<List<Cert>?>
}