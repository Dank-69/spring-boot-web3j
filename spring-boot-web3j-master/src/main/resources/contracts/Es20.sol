// SPDX-License-Identifier: Apache 2.0
pragma solidity ^0.8.20;

contract ERC20Test {

    // 状态变量：存储每个地址的代币余额

    mapping(address => uint256) private _balances;

    // 状态变量：存储授权关系（允许第三方花费自己的代币）

    mapping(address => mapping(address => uint256)) private _allowances;

    // 状态变量：代币总供应量（当前流通的所有代币数量总和）
    uint256 private _totalSupply;

    // 状态变量：代币名称（如"Bitcoin"）
    string private _name;

    // 状态变量：代币符号（如"BTC"，用于显示和交易标识）
    string private _symbol;

    // 状态变量：代币小数位数（表示1个代币可分割的最小单位数量，通常为18）

    uint8 private _decimals;

    event Transfer(address indexed from, address indexed to, uint256 value);


    event Approval(address indexed owner, address indexed spender, uint256 value);


    constructor() {
        _name = "HWMToken";  // 初始化代币名称为"HWMToken"
        _symbol = "HWM";     // 初始化代币符号为"HWM"
        _decimals = 18;      // 初始化小数位数为18（符合主流代币设计）
    }

    // 视图函数：返回代币名称

    function name() public view returns (string memory) {
        return _name;
    }

    // 视图函数：返回代币符号
    function symbol() public view returns (string memory) {
        return _symbol;
    }

    // 视图函数：返回代币小数位数
    function decimals() public view returns (uint8) {
        return _decimals;
    }

    // 视图函数：返回当前代币总供应量
    function totalSupply() public view returns (uint256) {
        return _totalSupply;
    }

    // 视图函数：查询指定地址的代币余额

    function balanceOf(address account) public view returns (uint256) {
        return _balances[account];
    }

    // 核心功能：从调用者地址向目标地址转账

    function transfer(address to, uint256 value) public returns (bool) {

        require(to != address(0), "ERC20: transfer to the zero address");

        require(_balances[msg.sender] >= value, "ERC20: transfer amount exceeds balance");


        _balances[msg.sender] -= value;
        _balances[to] += value;


        emit Transfer(msg.sender, to, value);
        return true;
    }

    // 核心功能：授权第三方（spender）花费调用者的代币

    function approve(address spender, uint256 value) public returns (bool) {

        require(spender != address(0), "ERC20: approve to the zero address");


        _allowances[msg.sender][spender] = value;


        emit Approval(msg.sender, spender, value);
        return true;
    }

    // 视图函数：查询授权额度（owner允许spender花费的剩余额度）

    function allowance(address owner, address spender) public view returns (uint256) {
        return _allowances[owner][spender];
    }

    // 核心功能：从指定地址（from）向目标地址（to）转账（需先通过approve授权）

    function transferFrom(address from, address to, uint256 value) public returns (bool) {

        require(from != address(0), "ERC20: transfer from the zero address");

        require(to != address(0), "ERC20: transfer to the zero address");

        require(_balances[from] >= value, "ERC20: transfer amount exceeds balance");


        uint256 currentAllowance = _allowances[from][msg.sender];

        require(currentAllowance >= value, "ERC20: transfer amount exceeds allowance");


        _allowances[from][msg.sender] = currentAllowance - value;

        _balances[from] -= value;
        _balances[to] += value;


        emit Transfer(from, to, value);
        return true;
    }

    // 核心功能：铸造新代币（增加总供应量，并分配给调用者）

    function mint(uint256 value) public {
        // 总供应量增加（新代币被创建）
        _totalSupply += value;

        _balances[msg.sender] += value;


        emit Transfer(address(0), msg.sender, value);
    }



    function burn(uint256 value) public {

        require(_balances[msg.sender] >= value, "ERC20: burn amount exceeds balance");


        _totalSupply -= value;

        _balances[msg.sender] -= value;


        emit Transfer(msg.sender, address(0), value);
    }
}
