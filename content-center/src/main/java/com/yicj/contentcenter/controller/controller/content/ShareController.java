package com.yicj.contentcenter.controller.controller.content;

import com.yicj.contentcenter.auth.CheckLogin;
import com.yicj.contentcenter.domain.dto.content.ShareDTO;
import com.yicj.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {
    private final ShareService shareService ;

    @CheckLogin
    @GetMapping("/{id}")
    public ShareDTO findById(@PathVariable Integer id){
        return this.shareService.findById(id) ;
    }
}
