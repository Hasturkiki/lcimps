package scau.lcimp.lcimps.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@ApiModel(value = "R", description = "加工后的数据返回对象")
public class R<T> {

    @ApiModelProperty("编码：200成功，0和其它数字为失败")
    private String code;

    @ApiModelProperty("附带信息")
    private String msg;

    @ApiModelProperty("返回数据")
    private T data;

    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.code = "00000";
        r.msg = "Data request success at " + LocalDateTime.now();
        r.data = object;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.code = "404";
        r.msg = msg + "; Data request fail at " + LocalDateTime.now();
        return r;
    }

    public static <T> R<T> response(T object, String errorMsg) {
        if (Objects.isNull(object))
            return R.error(errorMsg);
        return R.success(object);
    }

    public static R<String> response(boolean res, String successMsg, String errorMsg) {
        return res ? R.success(successMsg) : R.error(errorMsg);
    }
}
