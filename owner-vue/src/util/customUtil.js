export default {
    deepCopy : function (o) {
        let result = {};
        if (typeof o === "object" && o !== null)
            for (let i in o) result[i] = this.deepCopy(o[i]);
        else result = o;
        return result;
    }
}