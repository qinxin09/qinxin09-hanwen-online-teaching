package com.yws.resourcemanage.utils.files;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class FileObj {
    private List<FileDetail> folders;
    private List<FileDetail> files;
    private List<String> permission;

    // getters and setters
}
