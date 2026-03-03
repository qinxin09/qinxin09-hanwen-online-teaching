const qx = {};
qx.date=function(dataStr){
        var now;
        if(dataStr == null || dataStr == ""){
            now = new Date();
        }else{
            now = new Date(dataStr);
        }
        
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');
        return {
            year,
            month,
            day,
            hours,
            minutes,
            seconds,
            format:function(format){
                return format.replace(/yyyy/g, year).replace(/MM/g, month).replace(/dd/g, day).replace(/HH/g, hours).replace(/mm/g, minutes).replace(/ss/g, seconds);
            }
        }
    };
    
    qx.首字母排序=function(arr){
        arr.sort((a, b) => {
            const nameA = a.name[0].localeCompare(b.name[0], 'zh');
            return nameA;
        });
        return arr;
    }
    qx.getObjectKeys=function(obj){
        return Object.keys(obj);
    };
    qx.cp=function(obj){
        let new_obj = JSON.stringify(obj);
        return JSON.parse(new_obj);
    }
    qx.checkType=function(obj){
        return Object.prototype.toString.call(obj).slice(8, -1);
    }
    //用一个方法来判断对象是否为null、undefined
    qx.isNull=function(obj){
        return obj===null||obj===undefined;
    }
    qx.isEmpty=function(obj){
        if(qx.isNull(obj)){
            return true;
        }
        if(qx.checkType(obj)==='Object'){
            return Object.keys(obj).length===0;
        }
        return false;
    }
    qx.isArray=function(obj){
        return qx.checkType(obj)==='Array';
    }

// console.log(qx.date(new Date()).format('yyyy-MM-dd HH:mm:ss'));
export default qx;