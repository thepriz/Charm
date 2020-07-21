function initializeCoreMod() {
    var ASM_HOOKS = "svenhjol/charm/base/CharmAsmHooks";
    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var IincInsnNode = Java.type('org.objectweb.asm.tree.IincInsnNode');
    var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

    return {
        'no_anvil_minimum_xp': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.inventory.container.RepairContainer',
                'methodName': 'func_230303_b_', // canTakeStack, or at least used to be in 1.15
                'methodDesc': '(Lnet/minecraft/entity/player/PlayerEntity;Z)Z'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getOpcode() == Opcodes.IFLE) {
                        var label = new LabelNode();
                        newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "getMinimumRepairCost", "()I", ASM.MethodType.STATIC))
                        newInstructions.add(new JumpInsnNode(Opcodes.IF_ICMPLE, label));
                        newInstructions.add(label);

                        method.instructions.insert(instruction, newInstructions);
                        method.instructions.remove(instruction);
                        success = true;
                        break;
                    }
                }

                print("[Charm ASM] (NoAnvilMinimumXp) " + (success ? "Patched RepairContainer" : "Failed to patch RepairContainer"));
                return method;
            }
        }
    }
}