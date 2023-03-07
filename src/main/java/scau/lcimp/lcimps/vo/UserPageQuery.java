package scau.lcimp.lcimps.vo;

import lombok.Data;
import scau.lcimp.lcimps.common.BasePageQuery;

/**
 * 基础分页请求对象
 **/
@Data
public class UserPageQuery extends BasePageQuery {

    private String keywords;

    private Boolean isEnabled;

    private Integer type;
}
