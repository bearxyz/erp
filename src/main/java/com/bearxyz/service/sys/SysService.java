package com.bearxyz.service.sys;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.exception.NameRepeatedException;
import com.bearxyz.domain.po.sys.*;
import com.bearxyz.domain.vo.PermissionVO;
import com.bearxyz.common.TreeNode;
import com.bearxyz.domain.vo.RoleListVO;
import com.bearxyz.domain.vo.RoleVO;
import com.bearxyz.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by bearxyz on 2017/6/3.
 */

@Transactional
@Service
public class SysService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DictRepository dictRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ShiroService shiroService;

    public User saveUser(User user) throws NameRepeatedException {
        User u = userRepository.findByEmail(user.getEmail());
        if (u != null) throw new NameRepeatedException("邮箱重复");
        return userRepository.save(user);
    }

    public void deleteUserById(String id) {
        userRepository.delete(id);
    }

    public void deleteUsers(String[] ids) {
        for (String id : ids) {
            deleteUserById(id);
        }
    }

    public User getUserById(String id) {
        return userRepository.findOne(id);
    }

    public DataTable<User> getUsersByType(String type, int start, int length) {
        PageRequest request = new PageRequest(start / length, length, null);
        Page<User> users = userRepository.findUsersByType(type, request);
        for (User user : users) {
            Set<Role> roles = user.getRoles();
            String post = "";
            String dep = "";
            for (Role role : roles) {
                if (role.getType().equals("ROLE_TYPE_POST"))
                    post += role.getName() + " ";
                if (role.getType().equals("ROLE_TYPE_DEPARTMENT"))
                    dep += role.getName() + " ";
            }
            user.setPost(post);
            user.setDepartment(dep);
            user.setFullName(user.getFirstName()+user.getLastName());
        }
        DataTable dt = new DataTable<User>();
        dt.setData(users.getContent());
        dt.setRecordsTotal(users.getTotalElements());
        dt.setRecordsFiltered(users.getTotalElements());
        return dt;
    }

    public void setUserStatus(String id) {
        User user = getUserById(id);
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
    }

    public List<RoleListVO> getRoles() {
        List<RoleListVO> vo = new ArrayList<>();
        List<Dict> type = dictRepository.findAllByParentMask("ROLE_TYPE");
        for (Dict t : type) {
            RoleListVO v = new RoleListVO();
            v.setType(t.getName());
            List<Role> roles = roleRepository.findAllByType(t.getMask());
            List<RoleVO> rvo = new ArrayList<>();
            for (Role role : roles) {
                RoleVO vo1 = new RoleVO();
                BeanUtils.copyProperties(role, vo1);
                rvo.add(vo1);
            }
            v.setList(rvo);
            vo.add(v);
        }
        return vo;
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRoleById(String id) {
        roleRepository.delete(id);
    }

    public Role getRoleById(String id) {
        return roleRepository.findOne(id);
    }

    public DataTable<Role> getRolesByType(String type) {
        List<Role> roles = roleRepository.findAllByType(type);
        DataTable<Role> dt = new DataTable<>();
        dt.setRecordsFiltered((long) roles.size());
        dt.setRecordsTotal((long) roles.size());
        dt.setData(roles);
        return dt;
    }

    public void deleteRoles(String[] ids) {
        for (String id : ids) {
            deleteRoleById(id);
        }
    }

    public Dict saveDict(Dict dict) {
        if (dict.getParentId() != null && !dict.getParentId().equals("") && !dict.getParentId().equals(" ") && dictRepository.findOne(dict.getParentId()) != null) {
            Dict parent = dictRepository.findOne(dict.getParentId());
            dict.setParentMask(parent.getMask());
        } else
            dict.setParentId(" ");
        return dictRepository.save(dict);
    }

    public void deleteDicts(String[] ids) {
        for (String id : ids) {
            deleteDictById(id);
        }
    }

    public void deleteDictById(String id) {
        List<Dict> children = dictRepository.findAllByParentId(id);
        for (Dict dict : children) {
            deleteDictById(dict.getId());
        }
        dictRepository.delete(id);
    }

    public Dict getDictById(String id) {
        return dictRepository.findOne(id);
    }

    public Dict getDictByMask(String mask) {
        return dictRepository.findByMask(mask);
    }

    public List<TreeNode> getDictTreeByParentId(String pid) {
        List<Dict> dicts = getAllDictByParentId(pid);
        return dictListToTreeNode(dicts);
    }

    public List<TreeNode> getDictTreeByParentMask(String mask) {
        List<Dict> dicts = getAllDictByParentMask(mask);
        return dictListToTreeNodeWithMask(dicts);
    }

    private List<TreeNode> dictListToTreeNode(List<Dict> dicts) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Dict dict : dicts) {
            TreeNode node = new TreeNode();
            node.setId(dict.getId());
            node.setText(dict.getName());
            if (getCountByParentId(dict.getId()) > 0)
                node.setChildren(true);
            treeNodeList.add(node);
        }
        return treeNodeList;
    }

    private List<TreeNode> dictListToTreeNodeWithMask(List<Dict> dicts) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Dict dict : dicts) {
            TreeNode node = new TreeNode();
            node.setId(dict.getMask());
            node.setText(dict.getName());
            if (getCountByParentId(dict.getId()) > 0)
                node.setChildren(true);
            treeNodeList.add(node);
        }
        return treeNodeList;
    }

    public List<Dict> getAllDictByParentId(String pid) {
        return dictRepository.findAllByParentId(pid);
    }

    public List<Dict> getAllDictByParentMask(String mask) {
        return dictRepository.findAllByParentMask(mask);
    }

    public int getCountByParentId(String id) {
        return dictRepository.countAllByParentId(id);
    }

    public DataTable<Dict> getDictsByParentId(String pid, int start, int length) {
        PageRequest request = new PageRequest(start / length, length, null);
        if (pid == null || pid == "") pid = " ";
        Page<Dict> dicts = dictRepository.findDictsByParentId(pid, request);
        DataTable dt = new DataTable<Dict>();
        dt.setRecordsTotal(dicts.getTotalElements());
        dt.setRecordsFiltered(dicts.getTotalElements());
        dt.setData(dicts.getContent());
        return dt;
    }

    public Permission savePermission(Permission permission) {
        if (permission.getType().equals("PERMISSION_TYPE_MENU") && (permission.getId() == null || permission.getId().isEmpty()))
            permission.setSeq(permissionRepository.findMaxSeq() + 1);
        permissionRepository.save(permission);
        shiroService.updatePermission();
        return permission;
    }

    public List<PermissionVO> getAllPermission() {
        List<PermissionVO> results = new ArrayList<>();
        List<Permission> permissions = getPermissionMenu();
        for (Permission permission : permissions) {
            PermissionVO vo = new PermissionVO();
            List<Permission> children = getPermissionAction(permission.getId());
            List<PermissionVO> chil = new ArrayList<>();
            vo.setPermission(permission);
            for (Permission c : children) {
                PermissionVO v = new PermissionVO();
                v.setPermission(c);
                chil.add(v);
            }
            vo.setChildren(chil);
            results.add(vo);
        }
        return results;
    }

    public void deletePermissionById(String id) {
        permissionRepository.delete(id);
        shiroService.updatePermission();
    }

    public void deletePermissions(String[] ids) {
        for (String id : ids) {
            deletePermissionById(id);
        }
        shiroService.updatePermission();
    }

    public Permission getPermissionById(String id) {
        return permissionRepository.findOne(id);
    }

    public List<Permission> getPermissionAction(String pid) {
        return permissionRepository.findAllByParentIdOrderBySeqAsc(pid);
    }

    public DataTable<Permission> getPermissionActions(String pid) {
        DataTable<Permission> dt = new DataTable<>();
        List<Permission> permissions = permissionRepository.findAllByParentIdOrderBySeqAsc(pid);
        dt.setData(permissions);
        dt.setRecordsTotal((long) permissions.size());
        dt.setRecordsFiltered((long) permissions.size());
        return dt;
    }

    public List<Permission> getPermissionMenu() {
        return permissionRepository.findAllByTypeOrderBySeqAsc("PERMISSION_TYPE_MENU");
    }

    public List<TreeNode> getPermissionTree() {
        List<Permission> permissions = getPermissionMenu();
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Permission permission : permissions) {
            TreeNode node = new TreeNode();
            node.setId(permission.getId());
            node.setText(permission.getName());
            treeNodeList.add(node);
        }
        return treeNodeList;
    }

    public List<Permission> getUserPermissions(String userId) {
        return permissionRepository.findAllByUserId(userId);
    }

    public void assignPermissionsToRole(String roleId, String[] permission) {
        Role role = getRoleById(roleId);
        role.getPermissions().clear();
        for (String p : permission) {
            Permission perm = getPermissionById(p);
            role.getPermissions().add(perm);
        }
        roleRepository.save(role);
    }

    public void assignRolesToUser(String userId, String[] roles) {
        User user = getUserById(userId);
        user.getRoles().clear();
        for (String role : roles) {
            Role r = getRoleById(role);
            user.getRoles().add(r);
        }
        userRepository.save(user);
    }

    public void movePermission(String id, Integer position, Integer old_position) {
        Permission permission = permissionRepository.findOne(id);
        List<Permission> permissions;
        if (position > old_position) {
            permissions = permissionRepository.findAllBySeqSmallToBig(old_position, position);
            for (Permission other : permissions) {
                other.setSeq(other.getSeq() - 1);
                permissionRepository.save(other);
            }
        }
        if (position < old_position) {
            permissions = permissionRepository.findAllBySeqBigToSmall(position, old_position);
            for (Permission other : permissions) {
                other.setSeq(other.getSeq() + 1);
                permissionRepository.save(other);
            }
        }
        permission.setSeq(position);
    }

    public User getManagerByUid(String uid){
        return userRepository.findManagerByUid(uid);
    }

    public List<User> getDeparmentManager(){
        return userRepository.findDepartmentManager();
    }

}
