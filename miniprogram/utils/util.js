// utils/util.js - 工具函数

/**
 * 格式化时间
 * @param {Date} date 日期对象
 * @param {string} format 格式化模式
 * @returns {string}
 */
const formatTime = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) return '';
  if (typeof date === 'string') {
    date = new Date(date);
  }
  
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getHours();
  const minute = date.getMinutes();
  const second = date.getSeconds();

  const formatNumber = n => {
    n = n.toString();
    return n[1] ? n : '0' + n;
  };

  return format
    .replace('YYYY', year)
    .replace('MM', formatNumber(month))
    .replace('DD', formatNumber(day))
    .replace('HH', formatNumber(hour))
    .replace('mm', formatNumber(minute))
    .replace('ss', formatNumber(second));
};

/**
 * 格式化价格
 * @param {number} price 价格（分）
 * @returns {string}
 */
const formatPrice = (price) => {
  if (typeof price !== 'number') return '0.00';
  return price.toFixed(2);
};

/**
 * 防抖函数
 * @param {Function} fn 需要防抖的函数
 * @param {number} delay 延迟时间
 * @returns {Function}
 */
const debounce = (fn, delay = 300) => {
  let timer = null;
  return function(...args) {
    if (timer) clearTimeout(timer);
    timer = setTimeout(() => {
      fn.apply(this, args);
    }, delay);
  };
};

/**
 * 节流函数
 * @param {Function} fn 需要节流的函数
 * @param {number} delay 延迟时间
 * @returns {Function}
 */
const throttle = (fn, delay = 300) => {
  let lastTime = 0;
  return function(...args) {
    const now = Date.now();
    if (now - lastTime >= delay) {
      fn.apply(this, args);
      lastTime = now;
    }
  };
};

/**
 * 获取订单状态文本
 * @param {number} status 订单状态
 * @returns {string}
 */
const getOrderStatusText = (status) => {
  const statusMap = {
    0: '待处理',
    1: '制作中',
    2: '已完成',
    3: '已取消'
  };
  return statusMap[status] || '未知状态';
};

/**
 * 获取订单状态颜色
 * @param {number} status 订单状态
 * @returns {string}
 */
const getOrderStatusColor = (status) => {
  const colorMap = {
    0: '#e6a23c',
    1: '#409eff',
    2: '#67c23a',
    3: '#909399'
  };
  return colorMap[status] || '#909399';
};

/**
 * 生成唯一ID
 * @returns {string}
 */
const generateId = () => {
  return Date.now().toString(36) + Math.random().toString(36).substr(2, 9);
};

module.exports = {
  formatTime,
  formatPrice,
  debounce,
  throttle,
  getOrderStatusText,
  getOrderStatusColor,
  generateId
};
