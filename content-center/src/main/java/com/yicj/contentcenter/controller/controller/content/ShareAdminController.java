package com.yicj.contentcenter.controller.controller.content;

import com.yicj.contentcenter.domain.dto.content.ShareAuditDTO;
import com.yicj.contentcenter.domain.entity.content.Share;
import com.yicj.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareAdminController {
    private final ShareService shareService ;

    @PutMapping("/audit/{id}")
    public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO auditDTO){
        return this.shareService.auditById(id, auditDTO);
    }
}
