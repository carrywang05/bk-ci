import { showLoginPopup } from '@/utils/util'
import eventBus from './eventBus'
import store from '@/store'
const { platformInfo } = (store.state as any).platFormConfig
interface UrlParam {
    url: string
    refresh: boolean
}

interface titleParams {
    title: string,
    serverName: string
}

function iframeUtil (router: any) {
    const utilMap: ObjectMap = {}
    function init () {
        if (window.addEventListener) {
            window.addEventListener('message', onMessage)
        } else if (window.attachEvent) {
            window.attachEvent('onmessage', onMessage)
        }
    }

    function onMessage (e) {
        parseMessage(e.data)
    }

    function send (target, action, params) {
        target.postMessage({
            action,
            params
        }, '*')
    }
    utilMap.updateTabTitle = function ({ title, serverName }: titleParams): void {
        if (title) {
            document.title = title
        }
        if (!title && serverName) {
            document.title = `${serverName} | ${platformInfo.i18n.name || platformInfo.name} | ${platformInfo.i18n.brandName || platformInfo.brandName}`
        }
    }

    utilMap.syncUrl = function ({ url, refresh = false }: UrlParam): void {
        const pathname = `${location.pathname.replace(/^\/(\w+)\/(\w+)\/(\S+)$/, '/$1/$2')}${url}`
        if (refresh) {
            location.pathname = pathname
        } else {
            router.replace(pathname)
        }
    }

    utilMap.toggleLoginDialog = showLoginPopup

    utilMap.popProjectDialog = function (project: Project): void {
        eventBus.$emit('show-project-dialog', project)
    }
 
    utilMap.toggleProjectMenu = function (show): void {
        show ? eventBus.$emit('show-project-menu') : eventBus.$emit('hide-project-menu')
    }

    utilMap.syncTopProjectId = function ({ projectId }): void {
        eventBus.$emit('update-project-id', projectId)
    }

    utilMap.showTips = function (tips): void {
        if (tips.message === 'Network Error') {
            tips.message = '网络出现问题，请检查你的网络是否正常'
        }
        tips.message = tips.message || tips.msg || ''
        eventBus.$bkMessage({
            offsetY: 20,
            limit: 1,
            ...tips
        })
    }
 
    utilMap.syncLocale = function (target: object, locale: string) {
        send(target, 'syncLocale', locale)
    }
 
    utilMap.syncProjectList = function (target, projectList: object[]): void {
        send(target, 'syncProjectList', projectList)
    }

    utilMap.syncProjectId = function (target, projectId: string): void {
        send(target, 'receiveProjectId', projectId)
    }
    
    utilMap.syncUserInfo = function (target, userInfo: object): void {
        send(target, 'syncUserInfo', userInfo)
    }

    utilMap.goHome = function (target: object): void {
        send(target, 'backHome', '')
    }

    utilMap.leaveConfirmOrder = function (target): void {
        send(target, 'leaveConfirmOrder', '')
    }

    utilMap.leaveCancelOrder = function (target): void {
        send(target, 'leaveCancelOrder', '')
    }

    utilMap.leaveConfirm = function ({ content = '离开后，新编辑的数据将丢失', type, subHeader, theme, ...restConf }):void {
        const iframeBox: any = document.getElementById('iframe-box')
        eventBus.$bkInfo({
            type: type || theme,
            theme: theme || type,
            subTitle: content,
            subHeader: subHeader ? eventBus.$createElement('p', {}, subHeader) : null,
            ...restConf,
            confirmFn: () => {
                utilMap.leaveConfirmOrder(iframeBox.contentWindow)
            },
            cancelFn: () => {
                utilMap.leaveCancelOrder(iframeBox.contentWindow)
            }
        })
    }
 
    function parseMessage (data) {
        try {
            const cb = utilMap[data.action]
            if (typeof cb === 'function') {
                return cb(data.params)
            }
        } catch (e) {
            console.warn(e)
        }
    }

    init()

    return utilMap
}

export default iframeUtil
