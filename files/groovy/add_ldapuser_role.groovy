import groovy.json.JsonSlurper
import org.sonatype.nexus.security.user.UserNotFoundException
import org.sonatype.nexus.security.user.UserSearchCriteria
import org.sonatype.nexus.security.user.User
import org.sonatype.nexus.security.role.RoleIdentifier
import org.sonatype.nexus.security.user.UserManager
import org.sonatype.nexus.security.user.RoleMappingUserManager;

//parsed_args = new JsonSlurper().parseText(args)
try {

   def admin = security.securitySystem.getUser('admin')
   RoleIdentifier nxAdmin = admin.getRoles()[0]	

   
   //search for a user in LDAP
//   criteria = new UserSearchCriteria(userId: parsed_args.usernames, source: "LDAP")
   criteria = new UserSearchCriteria(userId: "clevasseur", source: "LDAP")

   users = security.securitySystem.searchUsers(criteria)
   //user.forEach { println it }
   //return users.toString()+" "+users[0].getUserId()
//   return users[0].class.package.name + " "+ users.size()+" "+users.toString()
   for(User u : users){
   //UserManager userManager = security.securitySystem.getUserManager(admin.getSource());
   UserManager userManager = security.securitySystem.getUserManager(u.getSource());
//   userManager.setUsersRoles(u.userId, admin.getSource(), [ nxAdmin ].toSet())

/*        try {
          RoleMappingUserManager roleMappingUserManager = (RoleMappingUserManager) userManager;
          roleMappingUserManager.setUsersRoles(
              u.getUserId(),
              u.getSource(),
              nxAdmin
          );
        }catch(Exception e){
	  return e.toString()
        }	
*/
    	
    u.roles = [ nxAdmin ] // = roleIds.collect{ new RoleIdentifier(DEFAULT_SOURCE, it)}
//   security.securitySystem.updateUser(u)
//    return u.toString()  	
    User userModified = security.setUserRoles('admin' /*u.userId*/, [ 'nx-admin' ]) 
//    return userModified.getRoles()

      if("clevasseur".equalsIgnoreCase(u.userId)){
	log.info("FOUNDED !!! " +u.userId )

//       return security.securitySystem.listRoles('LDAP')
      } 

      security.securitySystem.setUsersRoles(u.userId, 'LDAP', [nxAdmin].toSet())
      return u	
      //u.addRole(nxAdmin);

//      security.securitySystem.setUsersRoles(u.userId, 'LDAP', [nxAdmin].toSet())
//      security.securitySystem.updateUser(u)
   }
//    security.setUserRoles(parsed_args.usernames, parsed_args.ldap_id, [parsed_args.role] )

return users[0].getRoles()	
//return "OK"

} catch(UserNotFoundException unfe) {
    log.info("User not found "+criteria.toString(), unfe)
    return "User not found "+criteria.toString()+" "+unfe
}


return "FAIL" 
