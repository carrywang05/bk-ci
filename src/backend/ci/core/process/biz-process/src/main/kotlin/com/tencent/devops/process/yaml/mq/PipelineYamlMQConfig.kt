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
 *
 */
package com.tencent.devops.process.yaml.mq

import com.tencent.devops.common.event.dispatcher.pipeline.mq.MQ
import com.tencent.devops.common.event.dispatcher.pipeline.mq.Tools
import com.tencent.devops.common.event.dispatcher.trace.TraceEventDispatcher
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PipelineYamlMQConfig {

    @Bean
    fun traceEventDispatcher(rabbitTemplate: RabbitTemplate) = TraceEventDispatcher(rabbitTemplate)

    /**
     * yaml流水线触发交换机
     */
    @Bean
    fun pipelineYamlExchange(): DirectExchange {
        val directExchange = DirectExchange(MQ.EXCHANGE_PIPELINE_YAML_LISTENER, true, false)
        directExchange.isDelayed = true
        return directExchange
    }

    @Bean
    fun pipelineYamlEnableQueue() = Queue(MQ.QUEUE_PIPELINE_YAML_ENABLE_EVENT)

    @Bean
    fun pipelineYamlEnableQueueBind(
        @Autowired pipelineYamlEnableQueue: Queue,
        @Autowired pipelineYamlExchange: DirectExchange
    ): Binding {
        return BindingBuilder.bind(pipelineYamlEnableQueue).to(pipelineYamlExchange)
            .with(MQ.ROUTE_PIPELINE_YAML_ENABLE_EVENT)
    }

    @Bean
    fun pipelineYamlEnableContainer(
        @Autowired connectionFactory: ConnectionFactory,
        @Autowired pipelineYamlEnableQueue: Queue,
        @Autowired rabbitAdmin: RabbitAdmin,
        @Autowired pacExchange: PipelineYamlTriggerListener,
        @Autowired messageConverter: Jackson2JsonMessageConverter
    ): SimpleMessageListenerContainer {
        return Tools.createSimpleMessageListenerContainer(
            connectionFactory = connectionFactory,
            queue = pipelineYamlEnableQueue,
            rabbitAdmin = rabbitAdmin,
            buildListener = pacExchange,
            messageConverter = messageConverter,
            startConsumerMinInterval = 10000,
            consecutiveActiveTrigger = 5,
            concurrency = 10,
            maxConcurrency = 20
        )
    }

    @Bean
    fun pipelineYamlTriggerQueue() = Queue(MQ.QUEUE_PIPELINE_YAML_TRIGGER_EVENT)

    @Bean
    fun pipelineYamlTriggerQueueBind(
        @Autowired pipelineYamlTriggerQueue: Queue,
        @Autowired pipelineYamlExchange: DirectExchange
    ): Binding {
        return BindingBuilder.bind(pipelineYamlTriggerQueue).to(pipelineYamlExchange)
            .with(MQ.ROUTE_PIPELINE_YAML_TRIGGER_EVENT)
    }

    @Bean
    fun pipelineYamlTriggerContainer(
        @Autowired connectionFactory: ConnectionFactory,
        @Autowired pipelineYamlTriggerQueue: Queue,
        @Autowired rabbitAdmin: RabbitAdmin,
        @Autowired pipelineYamlTriggerListener: PipelineYamlTriggerListener,
        @Autowired messageConverter: Jackson2JsonMessageConverter
    ): SimpleMessageListenerContainer {
        return Tools.createSimpleMessageListenerContainer(
            connectionFactory = connectionFactory,
            queue = pipelineYamlTriggerQueue,
            rabbitAdmin = rabbitAdmin,
            buildListener = pipelineYamlTriggerListener,
            messageConverter = messageConverter,
            startConsumerMinInterval = 10000,
            consecutiveActiveTrigger = 5,
            concurrency = 30,
            maxConcurrency = 50
        )
    }
}
