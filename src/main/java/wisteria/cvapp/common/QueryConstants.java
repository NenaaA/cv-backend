package wisteria.cvapp.common;

public final class QueryConstants {
    //USER QUERIES
    public static final String getUserById="select *\n" +
            "from wisteria_user\n" +
            "where id=:id";
    //CV QUERIES
    public static final String getAllCvForUserId="select *\n" +
            "from cv\n" +
            "where user_id=:userId";
    public static final String getCvById="select *\n" +
            "from cv\n" +
            "where id=:cvId";
    //CV CATEGORY QUERIES
    public static final String getCvCategoriesByCvId="select *\n" +
            "from cv_category\n" +
            "where cv_id=:cvId";
    public static final String getCvCategoriesIdsByCvId="select cc.id\n" +
            "from cv inner join cv_category cc on cv.id = cc.cv_id\n" +
            "where cv.id=:cvId and cv.user_id=:userId";
    //CV CATEGORY DETAILS
    public static final String getCvDetails="select cc.id       as categoryId,\n" +
            "       cc.category as categoryName,\n" +
            "       ccd.label   as label,\n" +
            "       ccd.value   as value\n" +
            "from cv_category cc\n" +
            "         inner join cv_category_details ccd on cc.id = ccd.cv_category_id\n" +
            "where cc.id in :categoryIds\n" +
            "order by cc.category desc";

    //CONFIG
    public static final String getValuesByGroupAndAttribute="select value\n" +
            "from config\n" +
            "where \"group\"=:groupValue and attribute=:attributeValue";

    public static final String getConfigListByGroupAndAttribute="select *\n" +
            "from config\n" +
            "where \"group\"=:groupValue and attribute=:attributeValue";

}
