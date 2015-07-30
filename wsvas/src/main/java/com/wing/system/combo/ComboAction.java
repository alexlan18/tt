package com.wing.system.combo;

import com.wing.framework.cache.CodeCache;
import com.wing.framework.common.Combo;
import com.wing.system.codeinfo.model.SCodeInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 下拉框数据获取
 *
 * @author: panwb
 *
 * Date: 2014/6/26
 * Time: 14:37
 */
@Controller
@RequestMapping("/combo")
public class ComboAction {

    /**
     *
     * 获取代码信息的下拉框
     *
     * @param codeTypeId (代码类型值)
     * @param value （选择的代码值）
     * @param showWaitChoose (是否显示请选择....)
     * @return
     */
    @RequestMapping("/{codeTypeId}")
    @ResponseBody
    public List<Combo> getComboInfo(@PathVariable String codeTypeId, String value, boolean showWaitChoose) {

        List<Combo> results = new ArrayList<Combo>();
        List<SCodeInfo> codeInfoList = CodeCache.getInstance().getValue(codeTypeId);

        //是否显示请选择
        if(showWaitChoose) {
            Combo combo = new Combo();

            combo.setId("");
            combo.setText("请选择...");
            if(value == null || value.equals("")) {
                combo.setSelected(true);
            }

            results.add(combo);
        }

        if(codeInfoList != null) {

            for(SCodeInfo codeInfo : codeInfoList) {
                Combo combo = new Combo();

                combo.setId(codeInfo.getCodeValue());
                combo.setText(codeInfo.getCodeName());
                if(value != null && value.equals(combo.getId())) {
                    combo.setSelected(true);
                } else {
                    combo.setSelected(false);
                }
                results.add(combo);
            }
        }
        return results;
    }
}
