package com.xonro.apps.seal.event;

import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.bpms.util.DBSql;
import com.xonro.apps.seal.util.SealUtil;

/**
 * 印章作废流程结束后，将对应印章的状态改为3(作废状态)
 *
 * @author hjj
 *
 */
public class SealCancelAExeListener extends ExecuteListener {
    @Override
    public String getDescription() {
        return "印章作废流程结束后，将对应印章的状态改为3(作废状态)";
    }

    @Override
    public void execute(ProcessExecutionContext cont) throws Exception {
        // 流程ID
        String bindId = cont.getProcessInstance().getId();
        // 根据bindId判断当前流程是否正常结束
        // Active：活动(运行中)
        // Suspend：挂起(暂停)
        // End：结束(正常)
        // Terminate：终止+结束(异常，terminateEventDefinition)
        // terminateAndCompensate：终止+补偿+结束(异常，terminateEventDefinition)
        String CONTROLSTATE = DBSql
                .getString("SELECT CONTROLSTATE FROM WFC_PROCESS WHERE ID='"
                        + bindId + "'");
        if (CONTROLSTATE.equals("end")) {
            // 获取印章编号
            String sealNo = DBSql
                    .getString("select  SEAL_NO from BO_XR_SM_INVALID where BINDID='"
                            + bindId + "'");
            // 根据印章编号修改印章状态为3(作废状态)
            SealUtil.updateSealFlag(sealNo, 3);

        }

    }

}
