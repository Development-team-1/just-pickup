
const moment = require('moment');
const ACCESS_TOKEN_NAME = "accessToken";
const EXPIRED_TIME_NAME = "expiredTime";

export default {
  getToken() {
    return localStorage.getItem(ACCESS_TOKEN_NAME);
  },
  saveToken(token) {
    localStorage.setItem(ACCESS_TOKEN_NAME, token);
  },
  getExpiredTime() {
    return localStorage.getItem(EXPIRED_TIME_NAME);
  },
  saveExpiredTime(expiredTime) {
    localStorage.setItem(EXPIRED_TIME_NAME, expiredTime);
  },
  destroyAll() {
    localStorage.removeItem(ACCESS_TOKEN_NAME);
    localStorage.removeItem(EXPIRED_TIME_NAME);
  },
  isExpired() {
    if (this.getToken() == null) return true;
    if (this.getExpiredTime() == null) return true;

    const expiredTime = this.getExpiredTime();

    const expiredMoment = moment(expiredTime);
    let currentMoment = moment();

    const difference = moment.duration(expiredMoment.diff(currentMoment)).asSeconds();

    // console.log(tag, "expireMoment = ", expiredMoment, "currentMoment = ", currentMoment, "diff = ", difference);

    // 만료 30초 전일 경우 만료로 판단
    return difference <= 30;
  }
}