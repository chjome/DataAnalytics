import os
import subprocess

def dirBuild():
    subprocess.check_output("mkdir ~/Documents/PKI", shell=True)
    subprocess.check_output("mkdir -p ~/Documents/PKI/ca/root-ca/private ~/Documents/PKI/ca/root-ca/db ~/Documents/PKI/crl ~/Documents/PKI/certs", shell=True)
    subprocess.check_output("chmod 700 ~/Documents/PKI/ca/root-ca/private", shell=True)
    subprocess.check_output("mkdir -p ~/Documents/PKI/ca/issuing-ca/private ~/Documents/PKI/ca/issuing-ca/db ~/Documents/PKI/crl ~/Documents/PKI/certs", shell=True)
    subprocess.check_output("chmod 700 ~/Documents/PKI/ca/issuing-ca/private", shell=True)

def dbInit():
    #root CA
    subprocess.check_output("cp /dev/null ~/Documents/PKI/ca/root-ca/db/root-ca.db", shell=True)
    subprocess.check_output("cp /dev/null ~/Documents/PKI/ca/root-ca/db/root-ca.db.attr", shell=True)
    subprocess.check_output("echo 01 > ~/Documents/PKI/ca/root-ca/db/root-ca.crt.srl", shell=True)
    subprocess.check_output("echo 01 > ~/Documents/PKI/ca/root-ca/db/root-ca.crl.srl", shell=True)
    #issuing CA
    subprocess.check_output("cp /dev/null ~/Documents/PKI/ca/issuing-ca/db/issuing-ca.db", shell=True)
    subprocess.check_output("cp /dev/null ~/Documents/PKI/ca/issuing-ca/db/issuing-ca.db.attr", shell=True)
    subprocess.check_output("echo 01 > ~/Documents/PKI/ca/issuing-ca/db/issuing-ca.crt.srl", shell=True)
    subprocess.check_output("echo 01 > ~/Documents/PKI/ca/issuing-ca/db/issuing-ca.crl.srl", shell=True)

def rootBuilderCA():
    subprocess.check_output("openssl req -new -config ~/Documents/cnf_files/root-ca.cnf -out ~/Documents/PKI/ca/root-ca.csr -keyout ~/Documents/PKI/ca/root-ca/private/root-ca.key", shell=True)
    subprocess.check_output("openssl ca -selfsign -config ~/Documents/cnf_files/root-ca.cnf -in ~/Documents/PKI/ca/root-ca.csr -out ~/Documents/PKI/ca/root-ca/db/root-ca.crt -extensions root_ca_ext", shell=True)
    subprocess.check_output("openssl ca -gencrl -config ~/Documents/cnf_files/root-ca.cnf -out ~/Documents/PKI/crl/root-ca.crl", shell=True)

def issuingBuilderCA():
    subprocess.check_output("openssl req -new -config ~/Documents/cnf_files/issuing-ca.cnf -out ~/Documents/PKI/ca/issuing-ca.csr -keyout ~/Documents/PKI/ca/issuing-ca/private/issuing-ca.key", shell=True)
    subprocess.check_output("openssl ca -config ~/Documents/cnf_files/root-ca.cnf -in ~/Documents/PKI/ca/issuing-ca.csr -out ~/Documents/PKI/ca/issuing-ca/db/issuing-ca.crt -extensions signing_ca_ext", shell=True)
    subprocess.check_output("openssl ca -gencrl -config ~/Documents/cnf_files/issuing-ca.cnf -out ~/Documents/PKI/crl/issuing-ca.crl", shell=True)

def endCertBuilder():
    #TLS-Server
    subprocess.check_output("SAN=DNS:cs4371.local,DNS:www.cs4371.local openssl req -new -config ~/Documents/cnf_files/tls-server-cert.cnf -out ~/Documents/PKI/certs/cs4371.local.csr -keyout ~/Documents/PKI/certs/cs4371.local.key", shell=True)
    subprocess.check_output("openssl ca -config ~/Documents/cnf_files/issuing-ca.cnf -in ~/Documents/PKI/certs/cs4371.local.csr -out ~/Documents/PKI/certs/cs4371.local.crt -extensions server_ext", shell=True)
    #TLS-Client
    subprocess.check_output("openssl req -new -config ~/Documents/cnf_files/tls-client-cert.cnf -out ~/Documents/PKI/certs/cs4371.client.csr -keyout ~/Documents/PKI/certs/cs4371.client.key", shell=True)
    subprocess.check_output("openssl ca -config ~/Documents/cnf_files/issuing-ca.cnf -in ~/Documents/PKI/certs/cs4371.client.csr -out ~/Documents/PKI/certs/cs4371.client.crt -policy extern_pol -extensions client_ext", shell=True)

def PKCS12Bundle():
    #Must be done manually
    print("openssl pkcs12 -export -name  + name +  -caname + caName1 -caname -inkey ~/Documents/PKI/certs/cs4371.client.key -in ~/Documents/PKI/certs/cs4371.client.crt -certfile ~/Documents/PKI/ca/ca-chain.pem -out ~/Documents/PKI/certs/bundle.p12", shell=True)

def main():
    user = os.getlogin()
    dirCheck = "/home/" + user + "/Documents"
    print("CA Buidler 1.0")
    #Check if in proper folder
    if os.getcwd() == dirCheck and os.path.isdir("./cnf_files/"):
        print("Building Directories...")
        dirBuild()
        print("Initializing Database...")
        dbInit()
        print("Building CA...")
        rootBuilderCA()
        issuingBuilderCA()
        print("Creating End Certificates...")
        endCertBuilder()
        print("Create/Deploy Certificate...")
        PKCS12Bundle() #only prints suggestion command to bundle certs.
    else:
        print("Please move the file to your Documents folder. Located in /home/user/Documents \n Be sure to name your folder containing .cnf files to cnf_files within ~/Documents/cnf_files")
        exit()
    print("Done!")
    exit()
main()
