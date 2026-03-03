Git 全局设置:

git config --global user.name "xxxxxxxxxxxxxxx"
git config --global user.email "xxxxxxxxxxxxxxxxx@user.noreply.gitee.com"
创建 git 仓库:

mkdir hanwenonline
cd hanwenonline
git init 
touch README.md
git add README.md
git commit -m "first commit"
git remote add origin https://gitee.com/qx09/hanwenonline.git
git push -u origin "master"
已有仓库?

cd existing_git_repo
git remote add origin https://gitee.com/qx09/hanwenonline.git
git push -u origin "master"
---

添加到暂存区
git add .
git add 文件名
查看暂存区状态git status
查看暂存区缓存文件git diff --cached
清空暂存区(不影响当前关联的远程仓库)git reset
查看当前路径已关联的仓库git remote -v
关联远程仓库git remote add 远程仓库别名 
仓库地址解除关联的仓库git remote remove 关联仓库时的别名
创建分支git checkout -b 分支名称
提交暂存区的代码git commit -m "备注"
推送到仓库默认分支git push
推送到指定仓库分支git push -u 仓库别名 分支名称



---

Git 创建分支的命令是 `git branch`，具体的用法如下：

1. 创建一个新的分支并切换到该分支上：

   ```
   git checkout -b <branch-name>
   ```
   
   这个命令相当于执行了以下两个命令：
   
   ```
   git branch <branch-name>
   git checkout <branch-name>
   ```

2. 创建一个新的分支，但不切换到该分支上：

   ```
   git branch <branch-name>
   ```

如果您想查看当前的分支，可以使用 `git branch` 命令，它会列出所有的分支，以及当前所在的分支会被标记为 `*` 。例如：

```
$ git branch
* main
  features
```

上面的命令表示当前在 `main` 分支上，并且还有一个名为 `features` 的分支。如果想切换到 `features` 分支上，可以使用 `git checkout features` 命令。



## 创建作业：

> course.works.push({
>
> ​    id:workId,
> ​    courseId,
> ​    title,
> ​    topics,
> ​    startDateTime,
> ​    endDateTime
> })

## 发布作业

> classroom_works.push({classroomId,workId})

班级作业列表的获取：
course.works、classroom_works、classroomId
判断work是否已提交：
取出 work.id
用户的学习记录study_record 中找works_done[i].id==work.id

## 提交作业

> 就是更新 study_record.study_record

## 批改作业：

还是更新 study_record.study_record

----

手动修改数据库

study_record，删除影响：视频进度、提交的作业将被清空，相当于退出班级，需要重新加入班级，没有别的影响