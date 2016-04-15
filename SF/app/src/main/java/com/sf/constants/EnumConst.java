package com.sf.constants;

/**
 * 枚举常量
 *
 * @author wjh
 */
public class EnumConst {

    /**
     * 下拉刷新上拉加载 状态值
     */
    public enum HeadShowState {
        defaultState("默认状态", 1), beyondState("顶部超出状态", 2), loadingState(
                "正在加载状态", 3);

        private String name;
        private int ordinal;

        HeadShowState(String name, int ordinal) {
            // TODO Auto-generated constructor stub
            this.name = name;
            this.ordinal = ordinal;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOrdinal() {
            return ordinal;
        }

        public void setOrdinal(int ordinal) {
            this.ordinal = ordinal;
        }
    }

    /**
     * 网络加载数据状态
     *
     * @author WJH
     */
    public enum LoadType {
        Default, Loading, Refresh
    }
}
