import groovy.json.JsonSlurper
import org.sonatype.nexus.security.user.UserNotFoundException
import org.sonatype.nexus.security.user.UserSearchCriteria
import org.sonatype.nexus.security.user.User
import org.sonatype.nexus.security.role.RoleIdentifier
import org.sonatype.nexus.security.user.UserManager
import org.sonatype.nexus.security.user.RoleMappingUserManager;

parsed_args = new JsonSlurper().parseText(args)
//parsed_args = new JsonSlurper().parseText('{ "usernames": ["loginname1","loginname2"], "source": "LDAP"}')



//get the admin role
def admin = security.securitySystem.getUser('admin')
RoleIdentifier nxAdmin = admin.getRoles()[0]	

def result = [:] // only used for displaying something when used in command-line. Not used. Real result is stored by Nexus.

for (String username : parsed_args.usernames ){

   try {
       //search for a user in LDAP (or more)
       //   criteria = new UserSearchCriteria(userId: parsed_args.usernames, source: "LDAP")
       criteria = new UserSearchCriteria(userId: username, source: parsed_args.source )
       usersFounded = security.securitySystem.searchUsers(criteria)

       //set the nx-admin role for each user founded
       for(User u : usersFounded){
          if(! u.roles.toString().contains('nx-admin')){
               security.securitySystem.setUsersRoles(u.userId, parsed_args.source, [nxAdmin].toSet())
          }
	  result.put(u.userId, u.roles)	
       }
   } catch(UserNotFoundException unfe) {
       log.info("(add_ldapuser_role.groovy) User not found "+criteria.toString(), unfe)
   }

}
return result	
