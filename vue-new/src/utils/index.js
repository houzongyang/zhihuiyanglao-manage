/**
 * 格式化时间
 * @param {Date} date 日期对象
 * @param {String} fmt 格式字符串，默认为 yyyy-MM-dd HH:mm:ss
 * @returns {String} 格式化后的字符串
 */
export function formatTime(date, fmt = 'yyyy-MM-dd HH:mm:ss') {
    if (!date) return '';
    if (typeof date === 'string') {
        date = new Date(date.replace(/-/g, '/'));
    }
    if (typeof date === 'number') {
        date = new Date(date);
    }

    const o = {
        'M+': date.getMonth() + 1, // 月份
        'd+': date.getDate(), // 日
        'H+': date.getHours(), // 小时
        'm+': date.getMinutes(), // 分
        's+': date.getSeconds(), // 秒
        'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
        'S': date.getMilliseconds() // 毫秒
    };

    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }

    for (let k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
        }
    }

    return fmt;
}

/**
 * 存储数据到本地
 * @param {String} key 键名
 * @param {Object} data 数据
 */
export function saveToLocal(key, data) {
    localStorage.setItem(key, JSON.stringify(data));
}

/**
 * 从本地获取数据
 * @param {String} key 键名
 * @param {Object} defaultValue 默认值
 * @returns {Object} 数据对象
 */
export function getFromLocal(key, defaultValue = {}) {
    const data = localStorage.getItem(key);
    return data ? JSON.parse(data) : defaultValue;
}

/**
 * 从本地删除数据
 * @param {String} key 键名
 */
export function removeFromLocal(key) {
    localStorage.removeItem(key);
} 