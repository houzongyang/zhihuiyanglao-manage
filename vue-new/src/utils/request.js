import axios from 'axios'
import router from "@/router";

// 创建axios对象
const request = axios.create({
    baseURL: process.env.VUE_APP_BASEURL || 'http://localhost:9090',
    timeout: 30000
})

// request拦截器
// 请求发送前的处理，如统一添加token
request.interceptors.request.use(config => {
    console.log('发送请求:', config.url, config);

    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = JSON.parse(localStorage.getItem("xm-user") || '{}')
    config.headers['token'] = user.token

    return config
}, error => {
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
});

// response拦截器
// 响应后的统一处理
request.interceptors.response.use(
    response => {
        console.log('收到响应:', response);
        let res = response.data;

        // 兼容服务端返回字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '401') {
            router.push('/login')
        }
        return res;
    },
    error => {
        console.error('响应错误:', error);
        if (error.response) {
            console.error('错误状态码:', error.response.status);
            console.error('错误数据:', error.response.data);
            
            if (error.response.status === 401) {
                router.push('/login')
            }
        }
        return Promise.reject(error)
    }
)

export default request 